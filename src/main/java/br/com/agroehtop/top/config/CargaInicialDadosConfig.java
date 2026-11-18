package br.com.agroehtop.top.config;

import br.com.agroehtop.top.model.*;
import br.com.agroehtop.top.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class CargaInicialDadosConfig {

    @Bean
    public CommandLineRunner carregarDadosIniciais(
            FazendaRepository fazendaRepo,
            CulturaAgricolaRepository culturaRepo,
            SafraRepository safraRepo,
            MaquinaRepository maquinaRepo,
            ClienteRepository clienteRepo,
            VendaRepository vendaRepo,
            UsuarioRepository usuarioRepo,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Carga de Usuários e Perfis de Acesso (Spring Security)
            if (usuarioRepo.count() == 0) {
                usuarioRepo.save(new Usuario(
                        "Administrador do Sistema",
                        "admin",
                        passwordEncoder.encode("admin123"),
                        PerfilUsuario.ROLE_ADMIN
                ));

                usuarioRepo.save(new Usuario(
                        "Operador de Campo",
                        "operador",
                        passwordEncoder.encode("operador123"),
                        PerfilUsuario.ROLE_OPERADOR
                ));
            }

            if (fazendaRepo.count() == 0) {
                Fazenda f1 = fazendaRepo.save(new Fazenda("Fazenda Vista Alegre", new BigDecimal("1200.00"), "Uberaba", "MG"));
                Fazenda f2 = fazendaRepo.save(new Fazenda("Fazenda Santa Luzia", new BigDecimal("850.00"), "Ribeirão Preto", "SP"));
                Fazenda f3 = fazendaRepo.save(new Fazenda("Fazenda Terra Negra", new BigDecimal("1500.00"), "Rio Verde", "GO"));

                CulturaAgricola c1 = culturaRepo.save(new CulturaAgricola("Soja", "Sacas / ha"));
                CulturaAgricola c2 = culturaRepo.save(new CulturaAgricola("Milho", "Sacas / ha"));
                CulturaAgricola c3 = culturaRepo.save(new CulturaAgricola("Café", "Sacas / ha"));
                CulturaAgricola c4 = culturaRepo.save(new CulturaAgricola("Algodão", "Tons / ha"));

                Safra s1 = null;
                if (safraRepo.count() == 0) {
                    s1 = safraRepo.save(new Safra(f1, c1, LocalDate.now().minusMonths(2), null,
                            new BigDecimal("450.00"), new BigDecimal("65.00"), new BigDecimal("185000.00"), StatusSafra.EM_ANDAMENTO));

                    safraRepo.save(new Safra(f2, c2, LocalDate.now().minusMonths(1), null,
                            new BigDecimal("300.00"), new BigDecimal("80.00"), new BigDecimal("120000.00"), StatusSafra.EM_ANDAMENTO));

                    safraRepo.save(new Safra(f3, c3, LocalDate.now().plusWeeks(2), null,
                            new BigDecimal("200.00"), new BigDecimal("45.00"), new BigDecimal("95000.00"), StatusSafra.PLANEJADA));

                    safraRepo.save(new Safra(f1, c4, LocalDate.now().minusMonths(8), LocalDate.now().minusMonths(2),
                            new BigDecimal("500.00"), new BigDecimal("120.00"), new BigDecimal("320000.00"), StatusSafra.CONCLUIDA));
                }

                if (maquinaRepo.count() == 0) {
                    Maquina m1 = new Maquina("Trator John Deere 8R", 2022, 1450.5);
                    m1.setFazenda(f1);
                    maquinaRepo.save(m1);

                    Maquina m2 = new Maquina("Colheitadeira Case IH 8250", 2023, 820.0);
                    m2.setFazenda(f2);
                    maquinaRepo.save(m2);

                    Maquina m3 = new Maquina("Pulverizador Jacto Uniport 3030", 2021, 2100.0);
                    m3.setFazenda(f3);
                    maquinaRepo.save(m3);
                }

                if (clienteRepo.count() == 0) {
                    Cliente cli1 = clienteRepo.save(new Cliente("Cooperativa Agropecuária Regional", "12.345.678/0001-90"));
                    Cliente cli2 = clienteRepo.save(new Cliente("Exportadora Grãos do Brasil S.A.", "98.765.432/0001-10"));

                    if (vendaRepo.count() == 0 && s1 != null) {
                        vendaRepo.save(new Venda(s1, cli1, LocalDate.now().minusWeeks(1), new BigDecimal("5000.00"), new BigDecimal("128.50")));
                    }
                }
            }
        };
    }
}
