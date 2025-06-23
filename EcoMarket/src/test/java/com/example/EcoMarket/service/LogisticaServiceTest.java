package com.example.EcoMarket.service;

import com.example.EcoMarket.model.LogisticaModel;
import com.example.EcoMarket.repository.LogisticaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LogisticaServiceTest {

    private LogisticaRepository logisticaRepository;
    private LogisticaService logisticaService;

    @BeforeEach
    public void setUp() {
        logisticaRepository = mock(LogisticaRepository.class);
        logisticaService = new LogisticaService(logisticaRepository);
    }

    @Test
    public void testObtenerTodas() {
        LogisticaModel l1 = new LogisticaModel(1, "terrestre", "Santiago", LocalDate.now(), LocalDate.now().plusDays(1), "Juan");
        LogisticaModel l2 = new LogisticaModel(2, "aéreo", "Temuco", LocalDate.now(), LocalDate.now().plusDays(2), "Ana");
        when(logisticaRepository.findAll()).thenReturn(Arrays.asList(l1, l2));

        List<LogisticaModel> lista = logisticaService.obtenerTodas();

        assertEquals(2, lista.size());
        verify(logisticaRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarPorId_Existe() {
        LogisticaModel l = new LogisticaModel(1, "marítimo", "Valparaíso", LocalDate.now(), LocalDate.now().plusDays(3), "Carlos");
        when(logisticaRepository.findById(1)).thenReturn(Optional.of(l));

        Optional<LogisticaModel> resultado = logisticaService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals("Valparaíso", resultado.get().getDestino());
    }

    @Test
    public void testBuscarPorId_NoExiste() {
        when(logisticaRepository.findById(99)).thenReturn(Optional.empty());

        Optional<LogisticaModel> resultado = logisticaService.buscarPorId(99);

        assertFalse(resultado.isPresent());
    }

    @Test
    public void testGuardar() {
        LogisticaModel nueva = new LogisticaModel(3, "terrestre", "Copiapó", LocalDate.now(), LocalDate.now().plusDays(1), "Lucía");
        when(logisticaRepository.save(nueva)).thenReturn(nueva);

        LogisticaModel resultado = logisticaService.guardar(nueva);

        assertEquals("Copiapó", resultado.getDestino());
        verify(logisticaRepository).save(nueva);
    }

    @Test
    public void testActualizar_Existe() {
        LogisticaModel actualizada = new LogisticaModel(4, "aéreo", "Iquique", LocalDate.now(), LocalDate.now().plusDays(2), "Pedro");
        when(logisticaRepository.existsById(4)).thenReturn(true);
        when(logisticaRepository.save(actualizada)).thenReturn(actualizada);

        LogisticaModel resultado = logisticaService.actualizar(actualizada);

        assertNotNull(resultado);
        assertEquals("Iquique", resultado.getDestino());
    }

    @Test
    public void testActualizar_NoExiste() {
        LogisticaModel log = new LogisticaModel(5, "marítimo", "Puerto Montt", LocalDate.now(), LocalDate.now().plusDays(4), "Laura");
        when(logisticaRepository.existsById(5)).thenReturn(false);

        LogisticaModel resultado = logisticaService.actualizar(log);

        assertNull(resultado);
    }

    @Test
    public void testEliminar_Existe() {
        when(logisticaRepository.existsById(6)).thenReturn(true);

        boolean eliminado = logisticaService.eliminar(6);

        assertTrue(eliminado);
        verify(logisticaRepository).deleteById(6);
    }

    @Test
    public void testEliminar_NoExiste() {
        when(logisticaRepository.existsById(7)).thenReturn(false);

        boolean eliminado = logisticaService.eliminar(7);

        assertFalse(eliminado);
        verify(logisticaRepository, never()).deleteById(anyInt());
    }
}
