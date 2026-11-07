package br.com.agroehtop.top.controller;

import br.com.agroehtop.top.model.Venda;
import br.com.agroehtop.top.service.VendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Venda>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Venda> cadastrar(@RequestBody Venda entidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(entidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venda> atualizar(
            @PathVariable Long id,
            @RequestBody Venda entidade) {
        entidade.setId(id);
        return ResponseEntity.ok(service.atualizar(id, entidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
