package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.InventarioModel;
import com.example.EcoMarket.service.InventarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventarioControllerTest {

    private InventarioService inventarioService;
    private InventarioController inventarioController;

    @BeforeEach
    public void setUp() {
        inventarioService = mock(InventarioService.class);
        inventarioController = new InventarioController(inventarioService);
    }

    @Test
    public void testGetAllInventario() {
        InventarioModel producto1 = new InventarioModel("1", "Producto A", LocalDate.of(2025, 12, 31), "Chile");
        InventarioModel producto2 = new InventarioModel("2", "Producto B", LocalDate.of(2026, 1, 15), "Argentina");

        when(inventarioService.obtenerTodosLosProductos()).thenReturn(Arrays.asList(producto1, producto2));

        ResponseEntity<List<InventarioModel>> response = inventarioController.getAllInventario();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(inventarioService, times(1)).obtenerTodosLosProductos();
    }

    @Test
    public void testGetInventarioByIdFound() {
        InventarioModel producto = new InventarioModel("1", "Producto A", LocalDate.of(2025, 12, 31), "Chile");

        when(inventarioService.buscarPorId("1")).thenReturn(Optional.of(producto));

        ResponseEntity<InventarioModel> response = inventarioController.getInventarioByRut("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    public void testGetInventarioByIdNotFound() {
        when(inventarioService.buscarPorId("1")).thenReturn(Optional.empty());

        ResponseEntity<InventarioModel> response = inventarioController.getInventarioByRut("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateProducto() {
        InventarioModel producto = new InventarioModel("1", "Producto A", LocalDate.of(2025, 12, 31), "Chile");

        when(inventarioService.guardarProducto(producto)).thenReturn(producto);

        ResponseEntity<InventarioModel> response = inventarioController.createProducto(producto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    public void testUpdateProductoSuccess() {
        InventarioModel producto = new InventarioModel("1", "Producto A", LocalDate.of(2025, 12, 31), "Chile");

        when(inventarioService.actualizarProducto(producto)).thenReturn(producto);

        ResponseEntity<InventarioModel> response = inventarioController.updateProducto(producto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(producto, response.getBody());
    }

    @Test
    public void testUpdateProductoNotFound() {
        InventarioModel producto = new InventarioModel("1", "Producto A", LocalDate.of(2025, 12, 31), "Chile");

        when(inventarioService.actualizarProducto(producto)).thenReturn(null);

        ResponseEntity<InventarioModel> response = inventarioController.updateProducto(producto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteProductoSuccess() {
        when(inventarioService.eliminarProducto("1")).thenReturn(true);

        ResponseEntity<Void> response = inventarioController.deleteProducto("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteProductoNotFound() {
        when(inventarioService.eliminarProducto("1")).thenReturn(false);

        ResponseEntity<Void> response = inventarioController.deleteProducto("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
