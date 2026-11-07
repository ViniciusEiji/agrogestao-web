package br.com.agroehtop.top.service;

import br.com.agroehtop.top.model.Maquina;
import br.com.agroehtop.top.repository.MaquinaRepository;
import org.springframework.stereotype.Service;

@Service
public class MaquinaService extends CrudService<Maquina> {

    public MaquinaService(MaquinaRepository repository) {
        super(repository);
    }
}
