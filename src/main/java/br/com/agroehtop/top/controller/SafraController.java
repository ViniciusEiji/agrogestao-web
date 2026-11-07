package br.com.agroehtop.top.controller;

import br.com.agroehtop.top.model.Safra;
import br.com.agroehtop.top.service.SafraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/safras")
@CrossOrigin(origins = "*")
public class SafraController {

    private final SafraService service;

    public SafraController(SafraService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Safra>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Safra> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Safra> cadastrar(@RequestBody Safra entidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(entidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Safra> atualizar(
            @PathVariable Long id,
            @RequestBody Safra entidade) {
        entidade.setId(id);
        return ResponseEntity.ok(service.atualizar(id, entidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
