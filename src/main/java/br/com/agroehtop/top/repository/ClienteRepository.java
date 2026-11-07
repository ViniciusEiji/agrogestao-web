package br.com.agroehtop.top.repository;

import br.com.agroehtop.top.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
