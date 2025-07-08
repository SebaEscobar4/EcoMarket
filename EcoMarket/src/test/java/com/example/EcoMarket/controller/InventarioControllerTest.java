package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.InventarioModel;
import com.example.EcoMarket.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventarioModel producto;

    @BeforeEach
    void setUp() {
        producto = new InventarioModel(
                "123",
                "Manzanas",
                LocalDate.of(2025, 12, 31),
                "Chile"
        );
    }

    @Test
    void testObtenerTodosLosProductos() throws Exception {
        when(inventarioService.obtenerTodosLosProductos()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("123"))
                .andExpect(jsonPath("$[0].nombreProducto").value("Manzanas"));
    }

    @Test
    void testObtenerProductoPorId_Existente() throws Exception {
        when(inventarioService.buscarPorId("123")).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/inventario/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreProducto").value("Manzanas"));
    }

    @Test
    void testObtenerProductoPorId_NoExistente() throws Exception {
        when(inventarioService.buscarPorId("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/inventario/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearProducto() throws Exception {
        when(inventarioService.guardarProducto(any(InventarioModel.class))).thenReturn(producto);

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.nombreProducto").value("Manzanas"));
    }

    @Test
    void testActualizarProducto_Existente() throws Exception {
        when(inventarioService.actualizarProducto(any(InventarioModel.class))).thenReturn(producto);

        mockMvc.perform(put("/api/inventario/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void testActualizarProducto_NoExistente() throws Exception {
        when(inventarioService.actualizarProducto(any(InventarioModel.class))).thenReturn(null);

        mockMvc.perform(put("/api/inventario/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarProducto_Existente() throws Exception {
        when(inventarioService.eliminarProducto("123")).thenReturn(true);

        mockMvc.perform(delete("/api/inventario/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarProducto_NoExistente() throws Exception {
        when(inventarioService.eliminarProducto("999")).thenReturn(false);

        mockMvc.perform(delete("/api/inventario/999"))
                .andExpect(status().isNotFound());
    }
}
