package br.com.agroehtop.top.service;

import br.com.agroehtop.top.model.Safra;
import br.com.agroehtop.top.repository.SafraRepository;
import org.springframework.stereotype.Service;

@Service
public class SafraService extends CrudService<Safra> {

    public SafraService(SafraRepository repository) {
        super(repository);
    }
}
