package br.com.agroehtop.top.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maquinas")
public class Maquina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @Column(nullable = false, length = 80)
    private String tipo;

    @Column(name = "ano_fabricacao")
    private Integer anoFabricacao;

    @Column(name = "horas_uso", nullable = false)
    private double horasUso;

    @JsonIgnore


    @OneToMany(mappedBy = "maquina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Manutencao> manutencoes = new ArrayList<>();

    public Maquina() {
        // Construtor padrão exigido pelo JPA e Spring MVC.
    }

    public Maquina(String tipo, Integer anoFabricacao, double horasUso) {
        this.tipo = tipo;
        this.anoFabricacao = anoFabricacao;
        this.horasUso = horasUso;
    }

    public void setFazenda(Fazenda fazenda) { this.fazenda = fazenda; }

    public void adicionarManutencao(Manutencao manutencao) {
        manutencoes.add(manutencao);
        manutencao.setMaquina(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Fazenda getFazenda() { return fazenda; }
    public String getTipo() { return tipo; }
    public Integer getAnoFabricacao() { return anoFabricacao; }
    public double getHorasUso() { return horasUso; }
    public List<Manutencao> getManutencoes() { return List.copyOf(manutencoes); }

    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setAnoFabricacao(Integer anoFabricacao) { this.anoFabricacao = anoFabricacao; }
    public void setHorasUso(double horasUso) { this.horasUso = horasUso; }
}
