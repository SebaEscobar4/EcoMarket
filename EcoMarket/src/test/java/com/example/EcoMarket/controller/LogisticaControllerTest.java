package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.LogisticaModel;
import com.example.EcoMarket.service.LogisticaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LogisticaControllerTest {

    private LogisticaService logisticaService;
    private LogisticaController logisticaController;

    @BeforeEach
    public void setUp() {
        logisticaService = mock(LogisticaService.class);
        logisticaController = new LogisticaController(logisticaService);
    }

    @Test
    public void testObtenerTodas() {
        LogisticaModel log1 = new LogisticaModel(1, "terrestre", "Santiago", LocalDate.now(), LocalDate.now().plusDays(1), "Juan");
        LogisticaModel log2 = new LogisticaModel(2, "marítimo", "Valparaíso", LocalDate.now(), LocalDate.now().plusDays(3), "Ana");
        when(logisticaService.obtenerTodas()).thenReturn(Arrays.asList(log1, log2));

        ResponseEntity<List<LogisticaModel>> response = logisticaController.obtenerTodas();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testObtenerPorId_Existe() {
        LogisticaModel log = new LogisticaModel(1, "aéreo", "Iquique", LocalDate.now(), LocalDate.now().plusDays(2), "Carlos");
        when(logisticaService.buscarPorId(1)).thenReturn(Optional.of(log));

        ResponseEntity<LogisticaModel> response = logisticaController.obtenerPorId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("aéreo", response.getBody().getTipoTransporte());
    }

    @Test
    public void testObtenerPorId_NoExiste() {
        when(logisticaService.buscarPorId(999)).thenReturn(Optional.empty());

        ResponseEntity<LogisticaModel> response = logisticaController.obtenerPorId(999);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testCrear() {
        LogisticaModel nueva = new LogisticaModel(3, "terrestre", "Rancagua", LocalDate.now(), LocalDate.now().plusDays(1), "Lucía");
        when(logisticaService.guardar(nueva)).thenReturn(nueva);

        ResponseEntity<LogisticaModel> response = logisticaController.crear(nueva);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Rancagua", response.getBody().getDestino());
    }

    @Test
    public void testActualizar_Correcto() {
        LogisticaModel actualizada = new LogisticaModel(4, "aéreo", "La Serena", LocalDate.now(), LocalDate.now().plusDays(2), "Pedro");
        when(logisticaService.actualizar(actualizada)).thenReturn(actualizada);

        ResponseEntity<LogisticaModel> response = logisticaController.actualizar(4, actualizada);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pedro", response.getBody().getEncargado());
    }

    @Test
    public void testActualizar_IdDistinto() {
        LogisticaModel log = new LogisticaModel(5, "marítimo", "Antofagasta", LocalDate.now(), LocalDate.now().plusDays(5), "Laura");

        ResponseEntity<LogisticaModel> response = logisticaController.actualizar(99, log);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testEliminar_Existe() {
        when(logisticaService.eliminar(6)).thenReturn(true);

        ResponseEntity<Void> response = logisticaController.eliminar(6);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    public void testEliminar_NoExiste() {
        when(logisticaService.eliminar(7)).thenReturn(false);

        ResponseEntity<Void> response = logisticaController.eliminar(7);

        assertEquals(404, response.getStatusCodeValue());
    }
}
