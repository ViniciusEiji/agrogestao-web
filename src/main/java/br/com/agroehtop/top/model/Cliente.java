package br.com.agroehtop.top.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    private String documento;

    @JsonIgnore

    @OneToMany(mappedBy = "cliente")
    private List<Venda> vendas = new ArrayList<>();

    protected Cliente() {
        // Construtor exigido pelo JPA.
    }

    public Cliente(String nome, String documento) {
        this.nome = nome;
        this.documento = documento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public String getDocumento() { return documento; }
    public List<Venda> getVendas() { return List.copyOf(vendas); }

    public void setNome(String nome) { this.nome = nome; }
    public void setDocumento(String documento) { this.documento = documento; }
}
