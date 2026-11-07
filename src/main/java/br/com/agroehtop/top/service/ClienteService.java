package br.com.agroehtop.top.service;

import br.com.agroehtop.top.model.Cliente;
import br.com.agroehtop.top.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService extends CrudService<Cliente> {

    public ClienteService(ClienteRepository repository) {
        super(repository);
    }
}
