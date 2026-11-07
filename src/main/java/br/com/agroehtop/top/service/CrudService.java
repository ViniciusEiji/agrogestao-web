package br.com.agroehtop.top.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Operações CRUD compartilhadas pelas entidades.
 */
public abstract class CrudService<T> {
    private final JpaRepository<T, Long> repository;

    protected CrudService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<T> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public T buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Registro não encontrado."));
    }

    @Transactional
    public T salvar(T entidade) {
        return repository.save(entidade);
    }

    @Transactional
    public T atualizar(Long id, T entidade) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Registro não encontrado.");
        }
        return repository.save(entidade);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Registro não encontrado.");
        }
        repository.deleteById(id);
    }
}
