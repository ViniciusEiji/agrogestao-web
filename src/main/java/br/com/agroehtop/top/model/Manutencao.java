package br.com.agroehtop.top.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "manutencoes")
public class Manutencao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "maquina_id", nullable = false)
    private Maquina maquina;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal custo;

    protected Manutencao() {
        // Construtor exigido pelo JPA.
    }

    public Manutencao(LocalDate data, String descricao, BigDecimal custo) {
        this.data = data;
        this.descricao = descricao;
        this.custo = custo;
    }

    public void setMaquina(Maquina maquina) { this.maquina = maquina; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Maquina getMaquina() { return maquina; }
    public LocalDate getData() { return data; }
    public String getDescricao() { return descricao; }
    public BigDecimal getCusto() { return custo; }

    public void setData(LocalDate data) { this.data = data; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }
}
