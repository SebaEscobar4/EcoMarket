package com.example.EcoMarket.service;

import com.example.EcoMarket.model.InformeModel;
import com.example.EcoMarket.repository.InformeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InformeServiceTest {

    private InformeRepository informeRepository;
    private InformeService informeService;

    @BeforeEach
    void setUp() {
        informeRepository = mock(InformeRepository.class);
        informeService = new InformeService(informeRepository);
    }

    @Test
    void obtenerTodosLosInformes_retornaListaDeInformes() {
        List<InformeModel> lista = List.of(new InformeModel("1", "Titulo", "Desc", LocalDate.now(), "Autor"));
        when(informeRepository.findAll()).thenReturn(lista);

        List<InformeModel> resultado = informeService.obtenerTodosLosInformes();

        assertEquals(1, resultado.size());
        verify(informeRepository).findAll();
    }

    @Test
    void buscarPorId_existente_retornaInforme() {
        InformeModel informe = new InformeModel("1", "Titulo", "Desc", LocalDate.now(), "Autor");
        when(informeRepository.findById("1")).thenReturn(Optional.of(informe));

        Optional<InformeModel> resultado = informeService.buscarPorId("1");

        assertTrue(resultado.isPresent());
        assertEquals("1", resultado.get().getId());
    }

    @Test
    void guardarInforme_guardaYRetorna() {
        InformeModel informe = new InformeModel("1", "Nuevo", "Descripción", LocalDate.now(), "Autor");
        when(informeRepository.save(informe)).thenReturn(informe);

        InformeModel guardado = informeService.guardarInforme(informe);

        assertEquals("1", guardado.getId());
        verify(informeRepository).save(informe);
    }

    @Test
    void actualizarInforme_existente_actualizaYRetorna() {
        InformeModel original = new InformeModel("1", "Titulo", "Desc", LocalDate.now(), "Autor");
        InformeModel actualizado = new InformeModel("1", "Nuevo", "Nueva Desc", LocalDate.now(), "Nuevo Autor");

        when(informeRepository.findById("1")).thenReturn(Optional.of(original));
        when(informeRepository.save(any(InformeModel.class))).thenReturn(actualizado);

        InformeModel resultado = informeService.actualizarInforme(actualizado);

        assertNotNull(resultado);
        assertEquals("Nuevo", resultado.getTitulo());
    }

    @Test
    void actualizarInforme_noExistente_retornaNull() {
        InformeModel informe = new InformeModel("2", "X", "Y", LocalDate.now(), "Z");
        when(informeRepository.findById("2")).thenReturn(Optional.empty());

        InformeModel resultado = informeService.actualizarInforme(informe);

        assertNull(resultado);
    }

    @Test
    void eliminarInforme_existente_eliminaYRetornaTrue() {
        when(informeRepository.existsById("1")).thenReturn(true);
        doNothing().when(informeRepository).deleteById("1");

        boolean resultado = informeService.eliminarInforme("1");

        assertTrue(resultado);
        verify(informeRepository).deleteById("1");
    }

    @Test
    void eliminarInforme_noExistente_retornaFalse() {
        when(informeRepository.existsById("1")).thenReturn(false);

        boolean resultado = informeService.eliminarInforme("1");

        assertFalse(resultado);
    }
}
