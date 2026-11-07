package br.com.agroehtop.top.controller;

import br.com.agroehtop.top.model.Fazenda;
import br.com.agroehtop.top.service.FazendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fazendas")
@CrossOrigin(origins = "*")
public class FazendaController {

    private final FazendaService service;

    public FazendaController(FazendaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Fazenda>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fazenda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Fazenda> cadastrar(@RequestBody Fazenda entidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(entidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fazenda> atualizar(
            @PathVariable Long id,
            @RequestBody Fazenda entidade) {
        entidade.setId(id);
        return ResponseEntity.ok(service.atualizar(id, entidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
