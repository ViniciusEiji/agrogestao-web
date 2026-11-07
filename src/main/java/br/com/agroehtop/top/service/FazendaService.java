package br.com.agroehtop.top.service;

import br.com.agroehtop.top.model.Fazenda;
import br.com.agroehtop.top.repository.FazendaRepository;
import org.springframework.stereotype.Service;

@Service
public class FazendaService extends CrudService<Fazenda> {

    public FazendaService(FazendaRepository repository) {
        super(repository);
    }
}
