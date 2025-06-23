package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.InformeModel;
import com.example.EcoMarket.service.InformeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InformeControllerTest {

    private InformeService informeService;
    private InformeController controller;

    @BeforeEach
    void setUp() {
        informeService = mock(InformeService.class);
        controller = new InformeController(informeService);
    }

    @Test
    void obtenerTodos_retornaLista() {
        List<InformeModel> lista = List.of(new InformeModel("1", "Titulo", "Desc", LocalDate.now(), "Autor"));
        when(informeService.obtenerTodosLosInformes()).thenReturn(lista);

        ResponseEntity<List<InformeModel>> response = controller.obtenerTodos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void obtenerPorId_existente_retornaInforme() {
        InformeModel informe = new InformeModel("1", "Titulo", "Desc", LocalDate.now(), "Autor");
        when(informeService.buscarPorId("1")).thenReturn(Optional.of(informe));

        ResponseEntity<InformeModel> response = controller.obtenerPorId("1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("1", response.getBody().getId());
    }

    @Test
    void obtenerPorId_noExistente_retorna404() {
        when(informeService.buscarPorId("1")).thenReturn(Optional.empty());

        ResponseEntity<InformeModel> response = controller.obtenerPorId("1");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void crearInforme_retorna201() {
        InformeModel informe = new InformeModel("1", "Titulo", "Desc", LocalDate.now(), "Autor");
        when(informeService.guardarInforme(informe)).thenReturn(informe);

        ResponseEntity<InformeModel> response = controller.crearInforme(informe);

        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void actualizarInforme_idDistinto_retorna400() {
        InformeModel informe = new InformeModel("2", "X", "Y", LocalDate.now(), "Z");

        ResponseEntity<InformeModel> response = controller.actualizarInforme("1", informe);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void actualizarInforme_existente_retorna200() {
        InformeModel informe = new InformeModel("1", "X", "Y", LocalDate.now(), "Z");
        when(informeService.actualizarInforme(informe)).thenReturn(informe);

        ResponseEntity<InformeModel> response = controller.actualizarInforme("1", informe);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void actualizarInforme_noExiste_retorna404() {
        InformeModel informe = new InformeModel("1", "X", "Y", LocalDate.now(), "Z");
        when(informeService.actualizarInforme(informe)).thenReturn(null);

        ResponseEntity<InformeModel> response = controller.actualizarInforme("1", informe);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void eliminarInforme_existente_retorna204() {
        when(informeService.eliminarInforme("1")).thenReturn(true);

        ResponseEntity<Void> response = controller.eliminarInforme("1");

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void eliminarInforme_noExiste_retorna404() {
        when(informeService.eliminarInforme("1")).thenReturn(false);

        ResponseEntity<Void> response = controller.eliminarInforme("1");

        assertEquals(404, response.getStatusCodeValue());
    }
}
