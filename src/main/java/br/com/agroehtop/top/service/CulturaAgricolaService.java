package br.com.agroehtop.top.service;

import br.com.agroehtop.top.model.CulturaAgricola;
import br.com.agroehtop.top.repository.CulturaAgricolaRepository;
import org.springframework.stereotype.Service;

@Service
public class CulturaAgricolaService extends CrudService<CulturaAgricola> {

    public CulturaAgricolaService(CulturaAgricolaRepository repository) {
        super(repository);
    }
}
