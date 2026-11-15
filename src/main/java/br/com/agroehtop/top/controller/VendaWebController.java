package br.com.agroehtop.top.controller;

import br.com.agroehtop.top.model.Cliente;
import br.com.agroehtop.top.model.Venda;
import br.com.agroehtop.top.service.ClienteService;
import br.com.agroehtop.top.service.SafraService;
import br.com.agroehtop.top.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/vendas")
public class VendaWebController {

    private final VendaService vendaService;
    private final SafraService safraService;
    private final ClienteService clienteService;

    public VendaWebController(VendaService vendaService,
                              SafraService safraService,
                              ClienteService clienteService) {
        this.vendaService = vendaService;
        this.safraService = safraService;
        this.clienteService = clienteService;
    }

    private void carregarAtributos(Model model) {
        List<Venda> vendas = vendaService.listar();
        model.addAttribute("vendas", vendas);
        model.addAttribute("safras", safraService.listar());
        model.addAttribute("clientes", clienteService.listar());

        BigDecimal faturamentoTotal = vendas.stream()
                .map(Venda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("kpiTotalVendas", vendas.size());
        model.addAttribute("kpiFaturamentoTotal", faturamentoTotal);
    }

    @GetMapping
    public String listar(Model model) {
        if (!model.containsAttribute("venda")) {
            Venda v = new Venda();
            v.setData(LocalDate.now());
            model.addAttribute("venda", v);
        }
        carregarAtributos(model);
        return "vendas/index";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("venda") Venda venda,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            carregarAtributos(model);
            return "vendas/index";
        }

        try {
            if (venda.getId() == null) {
                vendaService.salvar(venda);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Venda registrada com sucesso!");
            } else {
                vendaService.atualizar(venda.getId(), venda);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Venda atualizada com sucesso!");
            }
            return "redirect:/vendas";
        } catch (Exception ex) {
            model.addAttribute("mensagemErro", "Erro ao processar venda: " + ex.getMessage());
            carregarAtributos(model);
            return "vendas/index";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Venda venda = vendaService.buscarPorId(id);
            model.addAttribute("venda", venda);
            model.addAttribute("editando", true);
            carregarAtributos(model);
            return "vendas/index";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Venda não encontrada.");
            return "redirect:/vendas";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vendaService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Registro de venda excluído com sucesso!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir venda: " + ex.getMessage());
        }
        return "redirect:/vendas";
    }
}
