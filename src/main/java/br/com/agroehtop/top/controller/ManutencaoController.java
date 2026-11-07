package br.com.agroehtop.top.controller;

import br.com.agroehtop.top.model.Manutencao;
import br.com.agroehtop.top.service.ManutencaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manutencoes")
@CrossOrigin(origins = "*")
public class ManutencaoController {

    private final ManutencaoService service;

    public ManutencaoController(ManutencaoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Manutencao>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manutencao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Manutencao> cadastrar(@RequestBody Manutencao entidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(entidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Manutencao> atualizar(
            @PathVariable Long id,
            @RequestBody Manutencao entidade) {
        entidade.setId(id);
        return ResponseEntity.ok(service.atualizar(id, entidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
