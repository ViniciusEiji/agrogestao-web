package br.com.agroehtop.top.model;

/**
 * Papéis de acesso ao sistema conforme exigência do edital:
 * - ROLE_ADMIN: Acesso total (CRUD completo, exclusão e configurações).
 * - ROLE_OPERADOR: Acesso operacional (visualizar relatórios e cadastrar safras/vendas, sem poder de exclusão).
 */
public enum PerfilUsuario {
    ROLE_ADMIN("Administrador"),
    ROLE_OPERADOR("Operador / Gestor");

    private final String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
