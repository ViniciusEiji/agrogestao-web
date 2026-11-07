package br.com.agroehtop.top.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "safras")
public class Safra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A fazenda é obrigatória.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @NotNull(message = "A cultura agrícola é obrigatória.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cultura_id", nullable = false)
    private CulturaAgricola cultura;

    @Column(name = "data_plantio")
    private LocalDate dataPlantio;

    @Column(name = "data_colheita")
    private LocalDate dataColheita;

    @NotNull(message = "A área plantada é obrigatória.")
    @DecimalMin(value = "0.01", message = "A área plantada deve ser maior que zero.")
    @Column(name = "area_plantada", nullable = false, precision = 12, scale = 2)
    private BigDecimal areaPlantada;

    @DecimalMin(value = "0.00", message = "A produtividade não pode ser negativa.")
    @Column(precision = 12, scale = 2)
    private BigDecimal produtividade;

    @DecimalMin(value = "0.00", message = "O custo de produção não pode ser negativo.")
    @Column(name = "custo_producao", precision = 14, scale = 2)
    private BigDecimal custoProducao;

    @NotNull(message = "O status do ciclo da safra é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusSafra status = StatusSafra.PLANEJADA;

    @JsonIgnore
    @OneToMany(mappedBy = "safra")
    private List<Venda> vendas = new ArrayList<>();

    public Safra() {
        // Construtor exigido pelo JPA.
    }

    public Safra(CulturaAgricola cultura, LocalDate dataPlantio,
                 LocalDate dataColheita, BigDecimal areaPlantada,
                 BigDecimal produtividade, BigDecimal custoProducao) {
        this.cultura = cultura;
        this.dataPlantio = dataPlantio;
        this.dataColheita = dataColheita;
        this.areaPlantada = areaPlantada;
        this.produtividade = produtividade;
        this.custoProducao = custoProducao;
        this.status = StatusSafra.PLANEJADA;
    }

    public Safra(Fazenda fazenda, CulturaAgricola cultura, LocalDate dataPlantio,
                 LocalDate dataColheita, BigDecimal areaPlantada,
                 BigDecimal produtividade, BigDecimal custoProducao, StatusSafra status) {
        this.fazenda = fazenda;
        this.cultura = cultura;
        this.dataPlantio = dataPlantio;
        this.dataColheita = dataColheita;
        this.areaPlantada = areaPlantada;
        this.produtividade = produtividade;
        this.custoProducao = custoProducao;
        this.status = (status != null) ? status : StatusSafra.PLANEJADA;
    }

    public void setFazenda(Fazenda fazenda) { this.fazenda = fazenda; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Fazenda getFazenda() { return fazenda; }
    public CulturaAgricola getCultura() { return cultura; }
    public LocalDate getDataPlantio() { return dataPlantio; }
    public LocalDate getDataColheita() { return dataColheita; }
    public BigDecimal getAreaPlantada() { return areaPlantada; }
    public BigDecimal getProdutividade() { return produtividade; }
    public BigDecimal getCustoProducao() { return custoProducao; }
    public StatusSafra getStatus() { return status; }
    public List<Venda> getVendas() { return List.copyOf(vendas); }

    public void setCultura(CulturaAgricola cultura) { this.cultura = cultura; }
    public void setDataPlantio(LocalDate dataPlantio) { this.dataPlantio = dataPlantio; }
    public void setDataColheita(LocalDate dataColheita) { this.dataColheita = dataColheita; }
    public void setAreaPlantada(BigDecimal areaPlantada) { this.areaPlantada = areaPlantada; }
    public void setProdutividade(BigDecimal produtividade) { this.produtividade = produtividade; }
    public void setCustoProducao(BigDecimal custoProducao) { this.custoProducao = custoProducao; }
    public void setStatus(StatusSafra status) { this.status = status; }
}
