package br.com.agroehtop.top.repository;

import br.com.agroehtop.top.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
