package br.com.agroehtop.top.service;

import br.com.agroehtop.top.exception.RegraNegocioException;
import br.com.agroehtop.top.model.Safra;
import br.com.agroehtop.top.model.StatusSafra;
import br.com.agroehtop.top.repository.SafraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class SafraService extends CrudService<Safra> {

    private final SafraRepository repository;

    public SafraService(SafraRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Valida as regras de negócio para criação ou atualização da safra.
     */
    public void validarSafra(Safra safra) {
        if (safra.getAreaPlantada() == null || safra.getAreaPlantada().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("A área plantada deve ser informada e maior que zero.");
        }

        // Validação de coerência temporal
        if (safra.getDataPlantio() != null && safra.getDataColheita() != null) {
            if (safra.getDataColheita().isBefore(safra.getDataPlantio())) {
                throw new RegraNegocioException("A data de colheita não pode ser anterior à data de plantio.");
            }
        }

        // Validação de consistência do status CONCLUÍDA
        if (safra.getStatus() == StatusSafra.CONCLUIDA) {
            if (safra.getDataColheita() == null) {
                throw new RegraNegocioException("Uma safra concluída deve possuir data de colheita informada.");
            }
            if (safra.getProdutividade() == null || safra.getProdutividade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RegraNegocioException("Uma safra concluída deve possuir produtividade final informada e positiva.");
            }
        }
    }

    /**
     * Valida e executa a transição de status de uma safra existente.
     */
    public void validarTransicaoStatus(StatusSafra statusAtual, StatusSafra novoStatus) {
        if (!statusAtual.podeTransitarPara(novoStatus)) {
            throw new RegraNegocioException(String.format(
                    "Transição inválida de status: não é permitido alterar de '%s' para '%s'.",
                    statusAtual.getDescricao(), novoStatus.getDescricao()
            ));
        }
    }

    @Override
    @Transactional
    public Safra salvar(Safra entidade) {
        if (entidade.getStatus() == null) {
            entidade.setStatus(StatusSafra.PLANEJADA);
        }
        validarSafra(entidade);
        return super.salvar(entidade);
    }

    @Override
    @Transactional
    public Safra atualizar(Long id, Safra entidade) {
        Safra safraExistente = buscarPorId(id);

        // Valida se a transição entre o status atual no banco e o pretendido é legítima
        if (entidade.getStatus() != null && entidade.getStatus() != safraExistente.getStatus()) {
            validarTransicaoStatus(safraExistente.getStatus(), entidade.getStatus());
        }

        validarSafra(entidade);
        entidade.setId(id);
        return super.atualizar(id, entidade);
    }

    /**
     * Atualização pontual de status da safra com validação estrita de transição.
     */
    @Transactional
    public Safra alterarStatus(Long id, StatusSafra novoStatus) {
        Safra safra = buscarPorId(id);
        validarTransicaoStatus(safra.getStatus(), novoStatus);
        safra.setStatus(novoStatus);
        validarSafra(safra);
        return repository.save(safra);
    }
}
