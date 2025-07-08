package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.TiendaModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TiendaRepositoryTest {

    @Autowired
    private TiendaRepository tiendaRepository;

    @Test
    public void testGuardarYBuscarTienda() {
        TiendaModel tienda = new TiendaModel("1", "Tienda Test", "Calle Test", "12345678", "test@mail.com");
        tiendaRepository.save(tienda);

        Optional<TiendaModel> found = tiendaRepository.findById("1");

        assertTrue(found.isPresent());
        assertEquals("Tienda Test", found.get().getNombre());
    }

    @Test
    public void testEliminarTienda() {
        TiendaModel tienda = new TiendaModel("2", "Tienda Borrar", "Calle Borrar", "87654321", "borrar@mail.com");
        tiendaRepository.save(tienda);

        tiendaRepository.deleteById("2");
        Optional<TiendaModel> found = tiendaRepository.findById("2");

        assertFalse(found.isPresent());
    }
}
