package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.LoginModel;
import com.example.EcoMarket.service.LoginService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    private LoginModel login1;
    private LoginModel login2;

    @BeforeEach
    void setUp() {
        login1 = new LoginModel("12345678-9", "Pedro", "José", "Pérez", "López", 912345678, "Calle 1", 1234567, "pedro@mail.com");
        login2 = new LoginModel("98765432-1", "Juan", "Carlos", "Ramírez", "Gómez", 987654321, "Calle 2", 7654321, "juan@mail.com");
    }

    @Test
    void testGetAllLogins() throws Exception {
        when(loginService.obtenerTodosLosLogins()).thenReturn(Arrays.asList(login1, login2));

        mockMvc.perform(get("/api/logins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.loginModelList").exists());
    }

    @Test
    void testGetLoginByRutFound() throws Exception {
        when(loginService.buscarLoginPorRut("12345678-9")).thenReturn(Optional.of(login1));

        mockMvc.perform(get("/api/logins/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombreP").value("Pedro"));
    }

    @Test
    void testGetLoginByRutNotFound() throws Exception {
        when(loginService.buscarLoginPorRut("00000000-0")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/logins/00000000-0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateLogin() throws Exception {
        when(loginService.guardarLogin(any(LoginModel.class))).thenReturn(login1);

        mockMvc.perform(post("/api/logins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(login1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void testUpdateLoginSuccess() throws Exception {
        when(loginService.actualizarLogin(any(LoginModel.class))).thenReturn(login1);

        mockMvc.perform(put("/api/logins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(login1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void testUpdateLoginNotFound() throws Exception {
        when(loginService.actualizarLogin(any(LoginModel.class))).thenReturn(null);

        mockMvc.perform(put("/api/logins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(login1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteLoginSuccess() throws Exception {
        when(loginService.eliminarLogin("12345678-9")).thenReturn(true);

        mockMvc.perform(delete("/api/logins/12345678-9"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteLoginNotFound() throws Exception {
        when(loginService.eliminarLogin("00000000-0")).thenReturn(false);

        mockMvc.perform(delete("/api/logins/00000000-0"))
                .andExpect(status().isNotFound());
    }

    // Conversor de objeto a JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
