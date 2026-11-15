package br.com.agroehtop.top.config;

import br.com.agroehtop.top.model.CulturaAgricola;
import br.com.agroehtop.top.model.Fazenda;
import br.com.agroehtop.top.model.Safra;
import br.com.agroehtop.top.model.StatusSafra;
import br.com.agroehtop.top.repository.CulturaAgricolaRepository;
import br.com.agroehtop.top.repository.FazendaRepository;
import br.com.agroehtop.top.repository.SafraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class CargaInicialDadosConfig {

    @Bean
    public CommandLineRunner carregarDadosIniciais(
            FazendaRepository fazendaRepo,
            CulturaAgricolaRepository culturaRepo,
            SafraRepository safraRepo) {

        return args -> {
            if (fazendaRepo.count() == 0) {
                Fazenda f1 = fazendaRepo.save(new Fazenda("Fazenda Vista Alegre", new BigDecimal("1200.00"), "Uberaba", "MG"));
                Fazenda f2 = fazendaRepo.save(new Fazenda("Fazenda Santa Luzia", new BigDecimal("850.00"), "Ribeirão Preto", "SP"));
                Fazenda f3 = fazendaRepo.save(new Fazenda("Fazenda Terra Negra", new BigDecimal("1500.00"), "Rio Verde", "GO"));

                CulturaAgricola c1 = culturaRepo.save(new CulturaAgricola("Soja", "Sacas / ha"));
                CulturaAgricola c2 = culturaRepo.save(new CulturaAgricola("Milho", "Sacas / ha"));
                CulturaAgricola c3 = culturaRepo.save(new CulturaAgricola("Café", "Sacas / ha"));
                CulturaAgricola c4 = culturaRepo.save(new CulturaAgricola("Algodão", "Tons / ha"));

                if (safraRepo.count() == 0) {
                    safraRepo.save(new Safra(f1, c1, LocalDate.now().minusMonths(2), null,
                            new BigDecimal("450.00"), new BigDecimal("65.00"), new BigDecimal("185000.00"), StatusSafra.EM_ANDAMENTO));

                    safraRepo.save(new Safra(f2, c2, LocalDate.now().minusMonths(1), null,
                            new BigDecimal("300.00"), new BigDecimal("80.00"), new BigDecimal("120000.00"), StatusSafra.EM_ANDAMENTO));

                    safraRepo.save(new Safra(f3, c3, LocalDate.now().plusWeeks(2), null,
                            new BigDecimal("200.00"), new BigDecimal("45.00"), new BigDecimal("95000.00"), StatusSafra.PLANEJADA));

                    safraRepo.save(new Safra(f1, c4, LocalDate.now().minusMonths(8), LocalDate.now().minusMonths(2),
                            new BigDecimal("500.00"), new BigDecimal("120.00"), new BigDecimal("320000.00"), StatusSafra.CONCLUIDA));
                }
            }
        };
    }
}
