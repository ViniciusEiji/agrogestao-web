package br.com.agroehtop.top.controller;

import br.com.agroehtop.top.model.Fazenda;
import br.com.agroehtop.top.service.FazendaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/fazendas")
public class FazendaWebController {

    private final FazendaService fazendaService;

    public FazendaWebController(FazendaService fazendaService) {
        this.fazendaService = fazendaService;
    }

    private void carregarAtributos(Model model) {
        List<Fazenda> fazendas = fazendaService.listar();
        model.addAttribute("fazendas", fazendas);

        BigDecimal areaTotal = fazendas.stream()
                .map(f -> f.getAreaHectares() != null ? f.getAreaHectares() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("kpiTotalFazendas", fazendas.size());
        model.addAttribute("kpiAreaTotalFazendas", areaTotal);
    }

    @GetMapping
    public String listar(Model model) {
        if (!model.containsAttribute("fazenda")) {
            model.addAttribute("fazenda", new Fazenda("", BigDecimal.ZERO, "", ""));
        }
        carregarAtributos(model);
        return "fazendas/index";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("fazenda") Fazenda fazenda,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            carregarAtributos(model);
            return "fazendas/index";
        }

        try {
            if (fazenda.getId() == null) {
                fazendaService.salvar(fazenda);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Fazenda cadastrada com sucesso!");
            } else {
                fazendaService.atualizar(fazenda.getId(), fazenda);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Fazenda atualizada com sucesso!");
            }
            return "redirect:/fazendas";
        } catch (Exception ex) {
            model.addAttribute("mensagemErro", "Erro ao salvar fazenda: " + ex.getMessage());
            carregarAtributos(model);
            return "fazendas/index";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Fazenda fazenda = fazendaService.buscarPorId(id);
            model.addAttribute("fazenda", fazenda);
            model.addAttribute("editando", true);
            carregarAtributos(model);
            return "fazendas/index";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Fazenda não encontrada.");
            return "redirect:/fazendas";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            fazendaService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Fazenda excluída com sucesso!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir a fazenda pois ela possui safras ou maquinários associados.");
        }
        return "redirect:/fazendas";
    }
}
