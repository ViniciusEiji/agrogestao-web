package br.com.agroehtop.top.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "vendas")
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private Safra safra;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 14, scale = 2)
    private BigDecimal precoUnitario;

    public Venda() {
        // Construtor padrão exigido pelo JPA e Spring MVC / formulários.
    }

    public Venda(Safra safra, Cliente cliente, LocalDate data,
                 BigDecimal quantidade, BigDecimal precoUnitario) {
        this.safra = safra;
        this.cliente = cliente;
        this.data = data;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Safra getSafra() { return safra; }
    public Cliente getCliente() { return cliente; }
    public LocalDate getData() { return data; }
    public BigDecimal getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }

    @Transient
    public BigDecimal getValorTotal() {
        if (quantidade == null || precoUnitario == null) {
            return BigDecimal.ZERO;
        }
        return quantidade.multiply(precoUnitario).setScale(2, RoundingMode.HALF_UP);
    }

    public void setSafra(Safra safra) { this.safra = safra; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setData(LocalDate data) { this.data = data; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
}
