package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.TiendaModel;
import com.example.EcoMarket.service.TiendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TiendaControllerTest {

    private MockMvc mockMvc;
    private TiendaService tiendaService;
    private TiendaController tiendaController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        tiendaService = Mockito.mock(TiendaService.class);
        tiendaController = new TiendaController(tiendaService);
        mockMvc = MockMvcBuilders.standaloneSetup(tiendaController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllTiendas() throws Exception {
        TiendaModel tienda1 = new TiendaModel("1", "Tienda 1", "Calle 123", "123456789", "tienda1@mail.com");
        TiendaModel tienda2 = new TiendaModel("2", "Tienda 2", "Avenida 456", "987654321", "tienda2@mail.com");

        when(tiendaService.obtenerTodasLasTiendas()).thenReturn(Arrays.asList(tienda1, tienda2));

        mockMvc.perform(get("/api/tiendas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].nombre").value("Tienda 2"));
    }

    @Test
    public void testGetTiendaById_Found() throws Exception {
        TiendaModel tienda = new TiendaModel("1", "Tienda 1", "Calle 123", "123456789", "tienda1@mail.com");

        when(tiendaService.buscarTiendaPorId("1")).thenReturn(Optional.of(tienda));

        mockMvc.perform(get("/api/tiendas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tienda 1"));
    }

    @Test
    public void testGetTiendaById_NotFound() throws Exception {
        when(tiendaService.buscarTiendaPorId("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tiendas/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTienda() throws Exception {
        TiendaModel tienda = new TiendaModel("1", "Tienda 1", "Calle 123", "123456789", "tienda1@mail.com");

        when(tiendaService.guardarTienda(any(TiendaModel.class))).thenReturn(tienda);

        mockMvc.perform(post("/api/tiendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tienda)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nombre").value("Tienda 1"));
    }

    @Test
    public void testUpdateTienda_Found() throws Exception {
        TiendaModel tienda = new TiendaModel("1", "Tienda Actualizada", "Calle 123", "123456789", "tienda1@mail.com");

        when(tiendaService.actualizarTienda(any(TiendaModel.class))).thenReturn(tienda);

        mockMvc.perform(put("/api/tiendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tienda)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Tienda Actualizada"));
    }

    @Test
    public void testUpdateTienda_NotFound() throws Exception {
        TiendaModel tienda = new TiendaModel("1", "Tienda Actualizada", "Calle 123", "123456789", "tienda1@mail.com");

        when(tiendaService.actualizarTienda(any(TiendaModel.class))).thenReturn(null);

        mockMvc.perform(put("/api/tiendas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tienda)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTienda_Deleted() throws Exception {
        when(tiendaService.eliminarTienda("1")).thenReturn(true);

        mockMvc.perform(delete("/api/tiendas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteTienda_NotFound() throws Exception {
        when(tiendaService.eliminarTienda("1")).thenReturn(false);

        mockMvc.perform(delete("/api/tiendas/1"))
                .andExpect(status().isNotFound());
    }
}
