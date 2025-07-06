package com.example.EcoMarket.service;

import com.example.EcoMarket.model.InventarioModel;
import com.example.EcoMarket.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventarioServiceTest {

    private InventarioRepository inventarioRepository;
    private InventarioService inventarioService;

    @BeforeEach
    void setUp() {
        inventarioRepository = Mockito.mock(InventarioRepository.class);
        inventarioService = new InventarioService(inventarioRepository);
    }

    @Test
    void testObtenerTodosLosProductos() {
        InventarioModel producto1 = new InventarioModel("1", "Producto A", LocalDate.now(), "Chile");
        InventarioModel producto2 = new InventarioModel("2", "Producto B", LocalDate.now(), "Perú");

        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(producto1, producto2));

        List<InventarioModel> productos = inventarioService.obtenerTodosLosProductos();

        assertEquals(2, productos.size());
        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorIdExistente() {
        InventarioModel producto = new InventarioModel("1", "Producto X", LocalDate.now(), "Argentina");

        when(inventarioRepository.findById("1")).thenReturn(Optional.of(producto));

        Optional<InventarioModel> resultado = inventarioService.buscarPorId("1");

        assertTrue(resultado.isPresent());
        assertEquals("Producto X", resultado.get().getNombreProducto());
    }

    @Test
    void testBuscarPorIdInexistente() {
        when(inventarioRepository.findById("99")).thenReturn(Optional.empty());

        Optional<InventarioModel> resultado = inventarioService.buscarPorId("99");

        assertFalse(resultado.isPresent());
    }

    @Test
    void testGuardarProducto() {
        InventarioModel nuevo = new InventarioModel("3", "Nuevo Producto", LocalDate.now(), "México");

        when(inventarioRepository.save(nuevo)).thenReturn(nuevo);

        InventarioModel guardado = inventarioService.guardarProducto(nuevo);

        assertNotNull(guardado);
        assertEquals("Nuevo Producto", guardado.getNombreProducto());
    }

    @Test
    void testActualizarProductoExistente() {
        InventarioModel original = new InventarioModel("1", "Original", LocalDate.of(2025, 1, 1), "Chile");
        InventarioModel actualizado = new InventarioModel("1", "Actualizado", LocalDate.of(2025, 12, 31), "Brasil");

        when(inventarioRepository.findById("1")).thenReturn(Optional.of(original));
        when(inventarioRepository.save(any(InventarioModel.class))).thenReturn(actualizado);

        InventarioModel resultado = inventarioService.actualizarProducto(actualizado);

        assertNotNull(resultado);
        assertEquals("Actualizado", resultado.getNombreProducto());
    }

    @Test
    void testActualizarProductoInexistente() {
        InventarioModel inventario = new InventarioModel("10", "Desconocido", LocalDate.now(), "Desconocido");

        when(inventarioRepository.findById("10")).thenReturn(Optional.empty());

        InventarioModel resultado = inventarioService.actualizarProducto(inventario);

        assertNull(resultado);
    }

    @Test
    void testEliminarProductoExistente() {
        when(inventarioRepository.existsById("1")).thenReturn(true);
        doNothing().when(inventarioRepository).deleteById("1");

        boolean resultado = inventarioService.eliminarProducto("1");

        assertTrue(resultado);
        verify(inventarioRepository).deleteById("1");
    }

    @Test
    void testEliminarProductoInexistente() {
        when(inventarioRepository.existsById("99")).thenReturn(false);

        boolean resultado = inventarioService.eliminarProducto("99");

        assertFalse(resultado);
    }
}
