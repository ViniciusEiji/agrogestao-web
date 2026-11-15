package br.com.agroehtop.top.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fazendas")
public class Fazenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "area_hectares", precision = 12, scale = 2)
    private BigDecimal areaHectares;

    @Column(nullable = false, length = 100)
    private String municipio;

    @Column(nullable = false, length = 2)
    private String uf;

    @JsonIgnore


    @OneToMany(mappedBy = "fazenda", cascade = CascadeType.ALL, orphanRemoval = true)


    private List<Safra> safras = new ArrayList<>();

    @JsonIgnore


    @OneToMany(mappedBy = "fazenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Maquina> maquinas = new ArrayList<>();

    public Fazenda() {
        // Construtor padrão exigido pelo JPA e Spring MVC.
    }

    public Fazenda(String nome, BigDecimal areaHectares, String municipio, String uf) {
        this.nome = nome;
        this.areaHectares = areaHectares;
        this.municipio = municipio;
        this.uf = uf;
    }

    public void adicionarSafra(Safra safra) {
        safras.add(safra);
        safra.setFazenda(this);
    }

    public void adicionarMaquina(Maquina maquina) {
        maquinas.add(maquina);
        maquina.setFazenda(this);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public BigDecimal getAreaHectares() { return areaHectares; }
    public String getMunicipio() { return municipio; }
    public String getUf() { return uf; }
    public List<Safra> getSafras() { return List.copyOf(safras); }
    public List<Maquina> getMaquinas() { return List.copyOf(maquinas); }

    public void setNome(String nome) { this.nome = nome; }
    public void setAreaHectares(BigDecimal areaHectares) { this.areaHectares = areaHectares; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public void setUf(String uf) { this.uf = uf; }
}
