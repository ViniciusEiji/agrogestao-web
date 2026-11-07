package br.com.agroehtop.top.service;

import br.com.agroehtop.top.model.Manutencao;
import br.com.agroehtop.top.repository.ManutencaoRepository;
import org.springframework.stereotype.Service;

@Service
public class ManutencaoService extends CrudService<Manutencao> {

    public ManutencaoService(ManutencaoRepository repository) {
        super(repository);
    }
}
