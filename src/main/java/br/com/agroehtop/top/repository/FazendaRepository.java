package br.com.agroehtop.top.repository;

import br.com.agroehtop.top.model.Fazenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FazendaRepository extends JpaRepository<Fazenda, Long> {
}
