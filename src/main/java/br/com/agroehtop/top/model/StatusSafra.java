package br.com.agroehtop.top.model;

/**
 * Representa os estados possíveis do ciclo de vida de uma Safra.
 */
public enum StatusSafra {
    PLANEJADA("Planejada"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusSafra(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Valida se a transição a partir do status atual para o novo status é permitida.
     * Regras de transição de ciclo:
     * - PLANEJADA pode ir para: EM_ANDAMENTO ou CANCELADA
     * - EM_ANDAMENTO pode ir para: CONCLUIDA ou CANCELADA
     * - CONCLUIDA: estado terminal, não pode ser alterado
     * - CANCELADA: estado terminal, não pode ser alterado
     */
    public boolean podeTransitarPara(StatusSafra novoStatus) {
        if (novoStatus == null || this == novoStatus) {
            return true;
        }

        return switch (this) {
            case PLANEJADA -> novoStatus == EM_ANDAMENTO || novoStatus == CANCELADA;
            case EM_ANDAMENTO -> novoStatus == CONCLUIDA || novoStatus == CANCELADA;
            case CONCLUIDA, CANCELADA -> false;
        };
    }
}
