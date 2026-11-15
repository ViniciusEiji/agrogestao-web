package br.com.agroehtop.top.controller;

import br.com.agroehtop.top.exception.RegraNegocioException;
import br.com.agroehtop.top.model.Safra;
import br.com.agroehtop.top.model.StatusSafra;
import br.com.agroehtop.top.service.CulturaAgricolaService;
import br.com.agroehtop.top.service.FazendaService;
import br.com.agroehtop.top.service.SafraService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping({"/", "/safras"})
public class SafraWebController {

    private final SafraService safraService;
    private final FazendaService fazendaService;
    private final CulturaAgricolaService culturaService;

    public SafraWebController(SafraService safraService,
                              FazendaService fazendaService,
                              CulturaAgricolaService culturaService) {
        this.safraService = safraService;
        this.fazendaService = fazendaService;
        this.culturaService = culturaService;
    }

    private void carregarAtributosComuns(Model model) {
        List<Safra> safras = safraService.listar();
        model.addAttribute("safras", safras);
        model.addAttribute("fazendas", fazendaService.listar());
        model.addAttribute("culturas", culturaService.listar());
        model.addAttribute("statusList", StatusSafra.values());

        // Cálculo de KPIs reais com base nas safras persistidas
        BigDecimal areaTotal = safras.stream()
                .map(Safra::getAreaPlantada)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long safrasAtivas = safras.stream()
                .filter(s -> s.getStatus() == StatusSafra.EM_ANDAMENTO || s.getStatus() == StatusSafra.PLANEJADA)
                .count();

        model.addAttribute("kpiAreaTotal", areaTotal);
        model.addAttribute("kpiSafrasAtivas", safrasAtivas);
        model.addAttribute("kpiTotalRegistros", safras.size());
    }

    @GetMapping
    public String index(Model model) {
        if (!model.containsAttribute("safra")) {
            Safra novaSafra = new Safra();
            novaSafra.setStatus(StatusSafra.PLANEJADA);
            model.addAttribute("safra", novaSafra);
        }
        carregarAtributosComuns(model);
        return "index";
    }

    @PostMapping("/salvar")
    public String salvarSafra(@Valid @ModelAttribute("safra") Safra safra,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            carregarAtributosComuns(model);
            return "index";
        }

        try {
            if (safra.getId() == null) {
                safraService.salvar(safra);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Safra cadastrada com sucesso!");
            } else {
                safraService.atualizar(safra.getId(), safra);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Safra atualizada com sucesso!");
            }
            return "redirect:/safras";
        } catch (RegraNegocioException ex) {
            model.addAttribute("mensagemErro", ex.getMessage());
            carregarAtributosComuns(model);
            return "index";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarSafra(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Safra safra = safraService.buscarPorId(id);
            model.addAttribute("safra", safra);
            carregarAtributosComuns(model);
            model.addAttribute("editando", true);
            return "index";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Safra não encontrada para edição.");
            return "redirect:/safras";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluirSafra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            safraService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Safra excluída com sucesso!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir safra: " + ex.getMessage());
        }
        return "redirect:/safras";
    }

    @PostMapping("/status/{id}")
    public String alterarStatusSafra(@PathVariable Long id,
                                     @RequestParam("novoStatus") StatusSafra novoStatus,
                                     RedirectAttributes redirectAttributes) {
        try {
            safraService.alterarStatus(id, novoStatus);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Status da safra alterado para " + novoStatus.getDescricao() + "!");
        } catch (RegraNegocioException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
        }
        return "redirect:/safras";
    }
}
