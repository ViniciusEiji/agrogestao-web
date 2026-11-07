package br.com.agroehtop.top.service;

import br.com.agroehtop.top.model.Venda;
import br.com.agroehtop.top.repository.VendaRepository;
import org.springframework.stereotype.Service;

@Service
public class VendaService extends CrudService<Venda> {

    public VendaService(VendaRepository repository) {
        super(repository);
    }
}
