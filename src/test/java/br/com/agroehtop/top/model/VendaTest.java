package br.com.agroehtop.top.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes Unitários - Entidade Venda e Cálculos Financeiros")
class VendaTest {

    @Test
    @DisplayName("Deve calcular o valor total corretamente a partir de quantidade e preço unitário")
    void deveCalcularValorTotalCorretamente() {
        Safra safra = new Safra();
        Cliente cliente = new Cliente("Cooperativa Central", "11.222.333/0001-44");

        Venda venda = new Venda(safra, cliente, LocalDate.now(),
                new BigDecimal("1500.00"), // 1500 sacas
                new BigDecimal("120.50")); // R$ 120,50 por saca

        BigDecimal valorEsperado = new BigDecimal("180750.00");
        assertEquals(valorEsperado, venda.getValorTotal());
    }

    @Test
    @DisplayName("Deve retornar zero no valor total quando quantidade ou preço forem nulos")
    void deveRetornarZeroQuandoCamposNulos() {
        Venda venda = new Venda();
        assertEquals(BigDecimal.ZERO, venda.getValorTotal());

        venda.setQuantidade(new BigDecimal("100.00"));
        assertEquals(BigDecimal.ZERO, venda.getValorTotal());
    }
}
