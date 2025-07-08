package com.example.EcoMarket.service;

import com.example.EcoMarket.model.PromocionModel;
import com.example.EcoMarket.repository.PromocionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PromocionServiceTest {

    private PromocionRepository promocionRepository;
    private PromocionService promocionService;

    @BeforeEach
    void setUp() {
        promocionRepository = mock(PromocionRepository.class);
        promocionService = new PromocionService(promocionRepository);
    }

    @Test
    void testObtenerTodas() {
        PromocionModel p1 = new PromocionModel(1, "Promo1", "Desc1", 10.0,
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));
        PromocionModel p2 = new PromocionModel(2, "Promo2", "Desc2", 15.0,
                LocalDate.of(2025, 2, 1), LocalDate.of(2025, 11, 30));

        when(promocionRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<PromocionModel> resultado = promocionService.obtenerTodas();

        assertEquals(2, resultado.size());
        verify(promocionRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorIdExistente() {
        PromocionModel promo = new PromocionModel(1, "Promo", "Desc", 12.0,
                LocalDate.of(2025, 3, 1), LocalDate.of(2025, 9, 30));
        when(promocionRepository.findById(1)).thenReturn(Optional.of(promo));

        Optional<PromocionModel> resultado = promocionService.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        verify(promocionRepository).findById(1);
    }

    @Test
    void testGuardar() {
        PromocionModel promo = new PromocionModel(3, "Promo Save", "Desc Save", 20.0,
                LocalDate.of(2025, 4, 1), LocalDate.of(2025, 10, 31));
        when(promocionRepository.save(promo)).thenReturn(promo);

        PromocionModel resultado = promocionService.guardar(promo);

        assertNotNull(resultado);
        assertEquals("Promo Save", resultado.getNombre());
        verify(promocionRepository).save(promo);
    }

    @Test
    void testActualizarExistente() {
        PromocionModel promo = new PromocionModel(4, "Promo Update", "Desc Update", 25.0,
                LocalDate.of(2025, 5, 1), LocalDate.of(2025, 11, 30));
        when(promocionRepository.existsById(4)).thenReturn(true);
        when(promocionRepository.save(promo)).thenReturn(promo);

        PromocionModel resultado = promocionService.actualizar(promo);

        assertNotNull(resultado);
        verify(promocionRepository).save(promo);
    }

    @Test
    void testActualizarNoExistente() {
        PromocionModel promo = new PromocionModel(5, "Promo Update", "Desc Update", 25.0,
                LocalDate.of(2025, 5, 1), LocalDate.of(2025, 11, 30));
        when(promocionRepository.existsById(5)).thenReturn(false);

        PromocionModel resultado = promocionService.actualizar(promo);

        assertNull(resultado);
        verify(promocionRepository, never()).save(any());
    }

    @Test
    void testEliminarExistente() {
        when(promocionRepository.existsById(6)).thenReturn(true);
        doNothing().when(promocionRepository).deleteById(6);

        boolean resultado = promocionService.eliminar(6);

        assertTrue(resultado);
        verify(promocionRepository).deleteById(6);
    }

    @Test
    void testEliminarNoExistente() {
        when(promocionRepository.existsById(7)).thenReturn(false);

        boolean resultado = promocionService.eliminar(7);

        assertFalse(resultado);
        verify(promocionRepository, never()).deleteById(anyInt());
    }
}