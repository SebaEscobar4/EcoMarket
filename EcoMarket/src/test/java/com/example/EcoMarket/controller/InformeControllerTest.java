package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.InformeModel;
import com.example.EcoMarket.service.InformeService;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(InformeController.class)
public class InformeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InformeService informeService;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON

    private InformeModel informe1;
    private InformeModel informe2;

    @BeforeEach
    void setup() {
        informe1 = new InformeModel("1", "Título 1", "Descripción 1", LocalDate.of(2025,7,8), "Autor1");
        informe2 = new InformeModel("2", "Título 2", "Descripción 2", LocalDate.of(2025,7,7), "Autor2");
    }

    @Test
    void testObtenerTodos() throws Exception {
        Mockito.when(informeService.obtenerTodosLosInformes())
                .thenReturn(Arrays.asList(informe1, informe2));

        mockMvc.perform(get("/api/v1/informes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.informeModelList").exists())
                .andExpect(jsonPath("$._embedded.informeModelList.length()").value(2))
                .andExpect(jsonPath("$._embedded.informeModelList[0].id").value("1"))
                .andExpect(jsonPath("$._embedded.informeModelList[1].id").value("2"));
    }

    @Test
    void testObtenerPorId_Existe() throws Exception {
        Mockito.when(informeService.buscarPorId("1"))
                .thenReturn(Optional.of(informe1));

        mockMvc.perform(get("/api/v1/informes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.titulo").value("Título 1"));
    }

    @Test
    void testObtenerPorId_NoExiste() throws Exception {
        Mockito.when(informeService.buscarPorId("999"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/informes/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearInforme() throws Exception {
        InformeModel nuevoInforme = new InformeModel("3", "Nuevo título", "Nueva descripción", LocalDate.now(), "Autor3");

        Mockito.when(informeService.guardarInforme(any(InformeModel.class))).thenReturn(nuevoInforme);

        mockMvc.perform(post("/api/v1/informes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoInforme)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/v1/informes/3"))
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.titulo").value("Nuevo título"));
    }

    @Test
    void testActualizarInforme_Exito() throws Exception {
        InformeModel actualizado = new InformeModel("1", "Título actualizado", "Descripción actualizada", LocalDate.now(), "Autor1");

        Mockito.when(informeService.actualizarInforme(any(InformeModel.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/informes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título actualizado"));
    }

    @Test
    void testActualizarInforme_IdNoCoincide() throws Exception {
        InformeModel actualizado = new InformeModel("2", "Título actualizado", "Descripción actualizada", LocalDate.now(), "Autor1");

        mockMvc.perform(put("/api/v1/informes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarInforme_NoEncontrado() throws Exception {
        InformeModel actualizado = new InformeModel("1", "Título actualizado", "Descripción actualizada", LocalDate.now(), "Autor1");

        Mockito.when(informeService.actualizarInforme(any(InformeModel.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/informes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarInforme_Exito() throws Exception {
        Mockito.when(informeService.eliminarInforme("1")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/informes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarInforme_NoEncontrado() throws Exception {
        Mockito.when(informeService.eliminarInforme("999")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/informes/999"))
                .andExpect(status().isNotFound());
    }

}
