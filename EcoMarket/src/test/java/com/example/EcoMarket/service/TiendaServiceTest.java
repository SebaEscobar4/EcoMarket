package com.example.EcoMarket.service;

import com.example.EcoMarket.model.TiendaModel;
import com.example.EcoMarket.repository.TiendaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TiendaServiceTest {

    private TiendaRepository tiendaRepository;
    private TiendaService tiendaService;

    @BeforeEach
    public void setUp() {
        tiendaRepository = Mockito.mock(TiendaRepository.class);
        tiendaService = new TiendaService(tiendaRepository);
    }

    @Test
    public void testObtenerTodasLasTiendas() {
        TiendaModel tienda1 = new TiendaModel("1", "Tienda 1", "Calle 1", "11111111", "t1@mail.com");
        TiendaModel tienda2 = new TiendaModel("2", "Tienda 2", "Calle 2", "22222222", "t2@mail.com");

        when(tiendaRepository.findAll()).thenReturn(Arrays.asList(tienda1, tienda2));

        List<TiendaModel> tiendas = tiendaService.obtenerTodasLasTiendas();

        assertEquals(2, tiendas.size());
        assertEquals("Tienda 1", tiendas.get(0).getNombre());
        assertEquals("Tienda 2", tiendas.get(1).getNombre());
    }

    @Test
    public void testBuscarTiendaPorId_Found() {
        TiendaModel tienda = new TiendaModel("1", "Tienda 1", "Calle 1", "11111111", "t1@mail.com");
        when(tiendaRepository.findById("1")).thenReturn(Optional.of(tienda));

        Optional<TiendaModel> result = tiendaService.buscarTiendaPorId("1");

        assertTrue(result.isPresent());
        assertEquals("Tienda 1", result.get().getNombre());
    }

    @Test
    public void testBuscarTiendaPorId_NotFound() {
        when(tiendaRepository.findById("1")).thenReturn(Optional.empty());

        Optional<TiendaModel> result = tiendaService.buscarTiendaPorId("1");

        assertFalse(result.isPresent());
    }

    @Test
    public void testGuardarTienda() {
        TiendaModel tienda = new TiendaModel("1", "Tienda 1", "Calle 1", "11111111", "t1@mail.com");

        when(tiendaRepository.save(any(TiendaModel.class))).thenReturn(tienda);

        TiendaModel saved = tiendaService.guardarTienda(tienda);

        assertNotNull(saved);
        assertEquals("Tienda 1", saved.getNombre());
    }

    @Test
    public void testActualizarTienda_Found() {
        TiendaModel tiendaExistente = new TiendaModel("1", "Tienda vieja", "Calle vieja", "00000000", "vieja@mail.com");
        TiendaModel tiendaActualizada = new TiendaModel("1", "Tienda nueva", "Calle nueva", "99999999", "nueva@mail.com");

        when(tiendaRepository.findById("1")).thenReturn(Optional.of(tiendaExistente));
        when(tiendaRepository.save(any(TiendaModel.class))).thenReturn(tiendaActualizada);

        TiendaModel result = tiendaService.actualizarTienda(tiendaActualizada);

        assertNotNull(result);
        assertEquals("Tienda nueva", result.getNombre());
        assertEquals("Calle nueva", result.getDireccion());
        assertEquals("99999999", result.getTelefono());
        assertEquals("nueva@mail.com", result.getCorreo());
    }

    @Test
    public void testActualizarTienda_NotFound() {
        TiendaModel tienda = new TiendaModel("1", "Tienda nueva", "Calle nueva", "99999999", "nueva@mail.com");

        when(tiendaRepository.findById("1")).thenReturn(Optional.empty());

        TiendaModel result = tiendaService.actualizarTienda(tienda);

        assertNull(result);
    }

    @Test
    public void testEliminarTienda_Exists() {
        when(tiendaRepository.existsById("1")).thenReturn(true);
        doNothing().when(tiendaRepository).deleteById("1");

        boolean result = tiendaService.eliminarTienda("1");

        assertTrue(result);
        verify(tiendaRepository, times(1)).deleteById("1");
    }

    @Test
    public void testEliminarTienda_NotExists() {
        when(tiendaRepository.existsById("1")).thenReturn(false);

        boolean result = tiendaService.eliminarTienda("1");

        assertFalse(result);
        verify(tiendaRepository, never()).deleteById("1");
    }
}
