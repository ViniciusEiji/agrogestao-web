package br.com.agroehtop.top.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "culturas_agricolas")
public class CulturaAgricola {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Column(name = "unidade_medida", nullable = false, length = 20)
    private String unidadeMedida;

    @JsonIgnore


    @OneToMany(mappedBy = "cultura")


    private List<Safra> safras = new ArrayList<>();

    protected CulturaAgricola() {
        // Construtor exigido pelo JPA.
    }

    public CulturaAgricola(String nome, String unidadeMedida) {
        this.nome = nome;
        this.unidadeMedida = unidadeMedida;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public String getUnidadeMedida() { return unidadeMedida; }
    public List<Safra> getSafras() { return List.copyOf(safras); }

    public void setNome(String nome) { this.nome = nome; }
    public void setUnidadeMedida(String unidadeMedida) { this.unidadeMedida = unidadeMedida; }
}
