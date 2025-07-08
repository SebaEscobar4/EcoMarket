package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.InventarioModel;
import com.example.EcoMarket.service.InventarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    private InventarioModel producto1;
    private InventarioModel producto2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        producto1 = new InventarioModel("1", "Producto A", LocalDate.of(2025, 12, 31), "Chile");
        producto2 = new InventarioModel("2", "Producto B", LocalDate.of(2026, 6, 30), "Argentina");
    }

    @Test
    void testGetAllInventario() {
        when(inventarioService.obtenerTodosLosProductos()).thenReturn(List.of(producto1, producto2));

        ResponseEntity<CollectionModel<EntityModel<InventarioModel>>> response = inventarioController.getAllInventario();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());

        verify(inventarioService, times(1)).obtenerTodosLosProductos();
    }

    @Test
    void testGetInventarioById_Found() {
        when(inventarioService.buscarPorId("1")).thenReturn(Optional.of(producto1));

        ResponseEntity<EntityModel<InventarioModel>> response = inventarioController.getInventarioById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getContent().getId());

        verify(inventarioService, times(1)).buscarPorId("1");
    }

    @Test
    void testGetInventarioById_NotFound() {
        when(inventarioService.buscarPorId("999")).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<InventarioModel>> response = inventarioController.getInventarioById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(inventarioService, times(1)).buscarPorId("999");
    }

    @Test
    void testCreateProducto() {
        when(inventarioService.guardarProducto(producto1)).thenReturn(producto1);

        ResponseEntity<EntityModel<InventarioModel>> response = inventarioController.createProducto(producto1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getContent().getId());
        assertTrue(response.getHeaders().getLocation().toString().contains("/api/inventario/1"));

        verify(inventarioService, times(1)).guardarProducto(producto1);
    }

    @Test
    void testUpdateProducto_Found() {
        when(inventarioService.actualizarProducto(producto1)).thenReturn(producto1);

        ResponseEntity<EntityModel<InventarioModel>> response = inventarioController.updateProducto("1", producto1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1", response.getBody().getContent().getId());

        verify(inventarioService, times(1)).actualizarProducto(producto1);
    }

    @Test
    void testUpdateProducto_NotFound() {
        when(inventarioService.actualizarProducto(producto1)).thenReturn(null);

        ResponseEntity<EntityModel<InventarioModel>> response = inventarioController.updateProducto("1", producto1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(inventarioService, times(1)).actualizarProducto(producto1);
    }

    @Test
    void testDeleteProducto_Deleted() {
        when(inventarioService.eliminarProducto("1")).thenReturn(true);

        ResponseEntity<Void> response = inventarioController.deleteProducto("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(inventarioService, times(1)).eliminarProducto("1");
    }

    @Test
    void testDeleteProducto_NotFound() {
        when(inventarioService.eliminarProducto("999")).thenReturn(false);

        ResponseEntity<Void> response = inventarioController.deleteProducto("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(inventarioService, times(1)).eliminarProducto("999");
    }
}
