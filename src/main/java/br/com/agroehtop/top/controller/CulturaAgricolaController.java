package br.com.agroehtop.top.controller;

import br.com.agroehtop.top.model.CulturaAgricola;
import br.com.agroehtop.top.service.CulturaAgricolaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/culturas")
@CrossOrigin(origins = "*")
public class CulturaAgricolaController {

    private final CulturaAgricolaService service;

    public CulturaAgricolaController(CulturaAgricolaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CulturaAgricola>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CulturaAgricola> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CulturaAgricola> cadastrar(@RequestBody CulturaAgricola entidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(entidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CulturaAgricola> atualizar(
            @PathVariable Long id,
            @RequestBody CulturaAgricola entidade) {
        entidade.setId(id);
        return ResponseEntity.ok(service.atualizar(id, entidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
