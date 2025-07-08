package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.PromocionModel;
import com.example.EcoMarket.service.PromocionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromocionController.class)
class PromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PromocionService promocionService;

    @Autowired
    private ObjectMapper objectMapper;

    private PromocionModel promocion;

    @BeforeEach
    void setUp() {
        promocion = new PromocionModel();
        promocion.setId(1);
        promocion.setNombre("Descuento 20%");
        promocion.setDescripcion("Oferta válida hasta fin de mes");
    }

    @Nested
    @DisplayName("GET /api/v1/promociones")
    class ObtenerPromociones {

        @Test
        @DisplayName("Debería retornar lista de promociones")
        void deberiaRetornarTodasLasPromociones() throws Exception {
            Mockito.when(promocionService.obtenerTodas()).thenReturn(Arrays.asList(promocion));

            mockMvc.perform(get("/api/v1/promociones"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id").value(promocion.getId()))
                    .andExpect(jsonPath("$[0].nombre").value(promocion.getNombre()));
        }

        @Test
        @DisplayName("Debería retornar promoción por ID si existe")
        void deberiaRetornarPromocionPorIdExistente() throws Exception {
            Mockito.when(promocionService.buscarPorId(1)).thenReturn(Optional.of(promocion));

            mockMvc.perform(get("/api/v1/promociones/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(promocion.getId()))
                    .andExpect(jsonPath("$.nombre").value(promocion.getNombre()));
        }

        @Test
        @DisplayName("Debería retornar 404 si la promoción no existe")
        void deberiaRetornarNotFoundSiNoExiste() throws Exception {
            Mockito.when(promocionService.buscarPorId(2)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/promociones/2"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/promociones")
    class CrearPromocion {

        @Test
        @DisplayName("Debería crear una nueva promoción")
        void deberiaCrearPromocion() throws Exception {
            Mockito.when(promocionService.guardar(any(PromocionModel.class))).thenReturn(promocion);

            mockMvc.perform(post("/api/v1/promociones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(promocion)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(promocion.getId()))
                    .andExpect(jsonPath("$.nombre").value(promocion.getNombre()));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/promociones/{id}")
    class ActualizarPromocion {

        @Test
        @DisplayName("Debería actualizar promoción si los IDs coinciden")
        void deberiaActualizarSiIdsCoinciden() throws Exception {
            Mockito.when(promocionService.actualizar(any(PromocionModel.class))).thenReturn(promocion);
            promocion.setId(1);

            mockMvc.perform(put("/api/v1/promociones/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(promocion)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(promocion.getId()))
                    .andExpect(jsonPath("$.nombre").value(promocion.getNombre()));
        }

        @Test
        @DisplayName("Debería retornar BadRequest si los IDs no coinciden")
        void deberiaRetornarBadRequestSiIdsNoCoinciden() throws Exception {
            promocion.setId(99);

            mockMvc.perform(put("/api/v1/promociones/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(promocion)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debería retornar NotFound si no se puede actualizar")
        void deberiaRetornarNotFoundSiNoExiste() throws Exception {
            promocion.setId(1);
            Mockito.when(promocionService.actualizar(any(PromocionModel.class))).thenReturn(null);

            mockMvc.perform(put("/api/v1/promociones/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(promocion)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/promociones/{id}")
    class EliminarPromocion {

        @Test
        @DisplayName("Debería eliminar promoción existente")
        void deberiaEliminarSiExiste() throws Exception {
            Mockito.when(promocionService.eliminar(1)).thenReturn(true);

            mockMvc.perform(delete("/api/v1/promociones/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debería retornar NotFound si la promoción no existe")
        void deberiaRetornarNotFoundSiNoExiste() throws Exception {
            Mockito.when(promocionService.eliminar(99)).thenReturn(false);

            mockMvc.perform(delete("/api/v1/promociones/99"))
                    .andExpect(status().isNotFound());
        }
    }
}