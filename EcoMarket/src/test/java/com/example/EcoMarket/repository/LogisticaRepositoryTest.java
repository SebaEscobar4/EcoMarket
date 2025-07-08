package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.LogisticaModel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LogisticaRepositoryTest {

    @Autowired
    private LogisticaRepository logisticaRepository;

    private LogisticaModel crearLogisticaEjemplo() {
        return new LogisticaModel(
                1,
                "Terrestre",
                "Santiago",
                LocalDate.of(2025, 6, 20),
                LocalDate.of(2025, 6, 22),
                "Juan Pérez"
        );
    }

    @Test
    void testGuardarYBuscarPorId() {
        LogisticaModel logistica = crearLogisticaEjemplo();
        logisticaRepository.save(logistica);

        Optional<LogisticaModel> resultado = logisticaRepository.findById(1);

        assertTrue(resultado.isPresent());
        assertEquals("Terrestre", resultado.get().getTipoTransporte());
        assertEquals("Santiago", resultado.get().getDestino());
        assertEquals(LocalDate.of(2025, 6, 22), resultado.get().getFechaEntrega());
    }

    @Test
    void testBuscarIdInexistente() {
        Optional<LogisticaModel> resultado = logisticaRepository.findById(999);
        assertFalse(resultado.isPresent());
    }

    @Test
    void testEliminarLogistica() {
        logisticaRepository.save(crearLogisticaEjemplo());

        logisticaRepository.deleteById(1);

        Optional<LogisticaModel> resultado = logisticaRepository.findById(1);
        assertFalse(resultado.isPresent());
    }
}
