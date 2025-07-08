package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.PromocionModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PromocionRepositoryTest {

    @Autowired
    private PromocionRepository promocionRepository;

    @Test
    @DisplayName("Guardar y buscar una promoción con todos los campos")
    public void testGuardarYBuscarPromocion() {
        // Crear un objeto PromocionModel con todos los campos
        PromocionModel promocion = new PromocionModel();
        promocion.setNombre("Oferta Verano");
        promocion.setDescripcion("Descuento especial de verano");
        promocion.setDescuento(15.5);
        promocion.setFechaInicio(LocalDate.of(2025, 7, 1));
        promocion.setFechaFin(LocalDate.of(2025, 7, 31));

        // Guardar promoción
        PromocionModel promocionGuardada = promocionRepository.save(promocion);

        // Verificar que se haya generado un ID
        assertNotNull(promocionGuardada.getId());

        // Buscar por id
        Optional<PromocionModel> promocionBuscada = promocionRepository.findById(promocionGuardada.getId());

        assertTrue(promocionBuscada.isPresent());

        PromocionModel p = promocionBuscada.get();
        assertEquals("Oferta Verano", p.getNombre());
        assertEquals("Descuento especial de verano", p.getDescripcion());
        assertEquals(15.5, p.getDescuento());
        assertEquals(LocalDate.of(2025, 7, 1), p.getFechaInicio());
        assertEquals(LocalDate.of(2025, 7, 31), p.getFechaFin());
    }
}