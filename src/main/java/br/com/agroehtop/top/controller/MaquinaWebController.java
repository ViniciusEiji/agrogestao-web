package br.com.agroehtop.top.controller;

import br.com.agroehtop.top.model.Maquina;
import br.com.agroehtop.top.service.FazendaService;
import br.com.agroehtop.top.service.MaquinaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/maquinas")
public class MaquinaWebController {

    private final MaquinaService maquinaService;
    private final FazendaService fazendaService;

    public MaquinaWebController(MaquinaService maquinaService, FazendaService fazendaService) {
        this.maquinaService = maquinaService;
        this.fazendaService = fazendaService;
    }

    private void carregarAtributos(Model model) {
        List<Maquina> maquinas = maquinaService.listar();
        model.addAttribute("maquinas", maquinas);
        model.addAttribute("fazendas", fazendaService.listar());

        double totalHoras = maquinas.stream()
                .mapToDouble(Maquina::getHorasUso)
                .sum();

        model.addAttribute("kpiTotalMaquinas", maquinas.size());
        model.addAttribute("kpiTotalHorasUso", totalHoras);
    }

    @GetMapping
    public String listar(Model model) {
        if (!model.containsAttribute("maquina")) {
            model.addAttribute("maquina", new Maquina("", 2024, 0.0));
        }
        carregarAtributos(model);
        return "maquinas/index";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("maquina") Maquina maquina,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            carregarAtributos(model);
            return "maquinas/index";
        }

        try {
            if (maquina.getId() == null) {
                maquinaService.salvar(maquina);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Maquinário cadastrado com sucesso!");
            } else {
                maquinaService.atualizar(maquina.getId(), maquina);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Maquinário atualizado com sucesso!");
            }
            return "redirect:/maquinas";
        } catch (Exception ex) {
            model.addAttribute("mensagemErro", "Erro ao salvar maquinário: " + ex.getMessage());
            carregarAtributos(model);
            return "maquinas/index";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Maquina maquina = maquinaService.buscarPorId(id);
            model.addAttribute("maquina", maquina);
            model.addAttribute("editando", true);
            carregarAtributos(model);
            return "maquinas/index";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Maquinário não encontrado.");
            return "redirect:/maquinas";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maquinaService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Maquinário excluído com sucesso!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir maquinário: " + ex.getMessage());
        }
        return "redirect:/maquinas";
    }
}
