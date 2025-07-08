package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.LogisticaModel;
import com.example.EcoMarket.service.LogisticaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LogisticaController.class)
public class LogisticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogisticaService logisticaService;

    @Autowired
    private ObjectMapper objectMapper;

    private LogisticaModel logistica;

    @BeforeEach
    public void setup() {
        logistica = new LogisticaModel(1, "terrestre", "Santiago", LocalDate.now(), LocalDate.now().plusDays(1), "Juan Pérez");
    }

    @Test
    public void testObtenerTodas() throws Exception {
        Mockito.when(logisticaService.obtenerTodas()).thenReturn(Arrays.asList(logistica));

        mockMvc.perform(get("/api/v1/logistica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.logisticaModelList[0].id").value(logistica.getId()));
    }

    @Test
    public void testObtenerPorId_Existe() throws Exception {
        Mockito.when(logisticaService.buscarPorId(1)).thenReturn(Optional.of(logistica));

        mockMvc.perform(get("/api/v1/logistica/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("tipoTransporte").value("terrestre"));
    }

    @Test
    public void testObtenerPorId_NoExiste() throws Exception {
        Mockito.when(logisticaService.buscarPorId(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/logistica/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrear() throws Exception {
        Mockito.when(logisticaService.guardar(any(LogisticaModel.class))).thenReturn(logistica);

        mockMvc.perform(post("/api/v1/logistica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logistica)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("tipoTransporte").value("terrestre"));
    }

    @Test
    public void testActualizar_Existe() throws Exception {
        Mockito.when(logisticaService.actualizar(any(LogisticaModel.class))).thenReturn(logistica);

        mockMvc.perform(put("/api/v1/logistica/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logistica)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1));
    }

    @Test
    public void testActualizar_IdDistinto() throws Exception {
        logistica.setId(2);
        mockMvc.perform(put("/api/v1/logistica/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logistica)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEliminar_Existe() throws Exception {
        Mockito.when(logisticaService.eliminar(1)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/logistica/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEliminar_NoExiste() throws Exception {
        Mockito.when(logisticaService.eliminar(999)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/logistica/999"))
                .andExpect(status().isNotFound());
    }
}
