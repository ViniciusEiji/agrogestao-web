package br.com.agroehtop.top.service;

import br.com.agroehtop.top.exception.RegraNegocioException;
import br.com.agroehtop.top.model.*;
import br.com.agroehtop.top.repository.SafraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - Regras de Negócio de Safra (SafraService)")
class SafraServiceTest {

    @Mock
    private SafraRepository repository;

    @InjectMocks
    private SafraService service;

    private Fazenda fazendaValida;
    private CulturaAgricola culturaValida;

    @BeforeEach
    void setUp() {
        fazendaValida = new Fazenda("Fazenda Experimental", new BigDecimal("1000.00"), "Ribeirão Preto", "SP");
        fazendaValida.setId(1L);

        culturaValida = new CulturaAgricola("Soja", "Sacas / ha");
        culturaValida.setId(1L);
    }

    @Nested
    @DisplayName("Cenários de Validação na Criação/Edição de Safra")
    class ValidacoesCampos {

        @Test
        @DisplayName("Deve permitir salvar safra quando todos os dados e regras forem válidos")
        void deveSalvarSafraValida() {
            Safra safra = new Safra(fazendaValida, culturaValida,
                    LocalDate.of(2026, 10, 1),
                    LocalDate.of(2027, 3, 1),
                    new BigDecimal("350.00"),
                    new BigDecimal("70.00"),
                    new BigDecimal("150000.00"),
                    StatusSafra.EM_ANDAMENTO);

            when(repository.save(any(Safra.class))).thenReturn(safra);

            Safra salva = service.salvar(safra);

            assertNotNull(salva);
            assertEquals(StatusSafra.EM_ANDAMENTO, salva.getStatus());
            verify(repository, times(1)).save(safra);
        }

        @Test
        @DisplayName("Deve rejeitar safra com área plantada nula ou menor/igual a zero")
        void deveRejeitarAreaInvalida() {
            Safra safraSemArea = new Safra(fazendaValida, culturaValida,
                    LocalDate.now(), null,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    StatusSafra.PLANEJADA);

            RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.salvar(safraSemArea));
            assertTrue(ex.getMessage().contains("área plantada deve ser informada e maior que zero"));
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar safra quando a data de colheita for anterior à data de plantio")
        void deveRejeitarDatasInconsistentes() {
            Safra safraDatasInvalidas = new Safra(fazendaValida, culturaValida,
                    LocalDate.of(2026, 11, 10), // Plantio
                    LocalDate.of(2026, 10, 5),  // Colheita anterior ao plantio
                    new BigDecimal("200.00"), BigDecimal.ZERO, BigDecimal.ZERO,
                    StatusSafra.EM_ANDAMENTO);

            RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.salvar(safraDatasInvalidas));
            assertTrue(ex.getMessage().contains("data de colheita não pode ser anterior à data de plantio"));
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve rejeitar status CONCLUÍDA se faltar data de colheita ou produtividade positiva")
        void deveRejeitarConcluidaIncompleta() {
            Safra safraIncompleta = new Safra(fazendaValida, culturaValida,
                    LocalDate.of(2026, 1, 1),
                    null, // Sem colheita
                    new BigDecimal("100.00"),
                    BigDecimal.ZERO, // Produtividade zerada
                    new BigDecimal("50000.00"),
                    StatusSafra.CONCLUIDA);

            RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.salvar(safraIncompleta));
            assertTrue(ex.getMessage().contains("safra concluída deve possuir data de colheita informada"));
        }
    }

    @Nested
    @DisplayName("Cenários da Máquina de Estados e Transições de Status")
    class TransicoesStatus {

        @Test
        @DisplayName("Deve permitir transição válida: PLANEJADA -> EM_ANDAMENTO")
        void devePermitirPlanejadaParaEmAndamento() {
            Safra existente = new Safra(fazendaValida, culturaValida, LocalDate.now(), null,
                    new BigDecimal("150.00"), null, null, StatusSafra.PLANEJADA);
            existente.setId(10L);

            when(repository.findById(10L)).thenReturn(Optional.of(existente));
            when(repository.save(any(Safra.class))).thenAnswer(i -> i.getArgument(0));

            Safra atualizada = service.alterarStatus(10L, StatusSafra.EM_ANDAMENTO);

            assertEquals(StatusSafra.EM_ANDAMENTO, atualizada.getStatus());
            verify(repository).save(existente);
        }

        @Test
        @DisplayName("Deve permitir transição válida: EM_ANDAMENTO -> CONCLUIDA (com dados completos)")
        void devePermitirEmAndamentoParaConcluida() {
            Safra existente = new Safra(fazendaValida, culturaValida,
                    LocalDate.now().minusMonths(4),
                    LocalDate.now(),
                    new BigDecimal("250.00"),
                    new BigDecimal("68.00"),
                    new BigDecimal("100000.00"),
                    StatusSafra.EM_ANDAMENTO);
            existente.setId(20L);

            when(repository.findById(20L)).thenReturn(Optional.of(existente));
            when(repository.save(any(Safra.class))).thenAnswer(i -> i.getArgument(0));

            Safra atualizada = service.alterarStatus(20L, StatusSafra.CONCLUIDA);

            assertEquals(StatusSafra.CONCLUIDA, atualizada.getStatus());
        }

        @Test
        @DisplayName("Deve bloquear transição inválida: CONCLUIDA não pode voltar para EM_ANDAMENTO")
        void deveBloquearConcluidaParaEmAndamento() {
            Safra safraConcluida = new Safra(fazendaValida, culturaValida,
                    LocalDate.now().minusMonths(6), LocalDate.now().minusMonths(1),
                    new BigDecimal("300.00"), new BigDecimal("75.00"), new BigDecimal("200000.00"),
                    StatusSafra.CONCLUIDA);
            safraConcluida.setId(30L);

            when(repository.findById(30L)).thenReturn(Optional.of(safraConcluida));

            RegraNegocioException ex = assertThrows(RegraNegocioException.class, () ->
                    service.alterarStatus(30L, StatusSafra.EM_ANDAMENTO));

            assertTrue(ex.getMessage().contains("Transição inválida de status"));
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Deve bloquear transição inválida: CANCELADA não pode transitar para nenhum status")
        void deveBloquearTransicaoDeCancelada() {
            Safra safraCancelada = new Safra(fazendaValida, culturaValida,
                    LocalDate.now(), null,
                    new BigDecimal("100.00"), null, null,
                    StatusSafra.CANCELADA);
            safraCancelada.setId(40L);

            when(repository.findById(40L)).thenReturn(Optional.of(safraCancelada));

            RegraNegocioException ex = assertThrows(RegraNegocioException.class, () ->
                    service.alterarStatus(40L, StatusSafra.PLANEJADA));

            assertTrue(ex.getMessage().contains("Transição inválida de status"));
        }
    }
}
