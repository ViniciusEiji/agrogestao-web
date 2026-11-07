package br.com.agroehtop.top.repository;

import br.com.agroehtop.top.model.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {
}
