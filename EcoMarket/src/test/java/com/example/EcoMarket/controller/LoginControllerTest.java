package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.LoginModel;
import com.example.EcoMarket.service.LoginService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private LoginService loginService;
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginService = mock(LoginService.class);
        loginController = new LoginController(loginService);
    }

    private LoginModel crearLoginEjemplo() {
        return new LoginModel(
                "12345678-9",
                "Juan",
                "Carlos",
                "Pérez",
                "Gómez",
                912345678,
                "Av. Siempre Viva 123",
                8320000,
                "juan@example.com"
        );
    }

    @Test
    void testGetAllLogins() {
        List<LoginModel> logins = Arrays.asList(crearLoginEjemplo());

        when(loginService.obtenerTodosLosLogins()).thenReturn(logins);

        ResponseEntity<List<LoginModel>> response = loginController.getAllLogins();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(loginService, times(1)).obtenerTodosLosLogins();
    }

    @Test
    void testGetLoginByRut_Found() {
        LoginModel login = crearLoginEjemplo();
        when(loginService.buscarLoginPorRut("12345678-9")).thenReturn(Optional.of(login));

        ResponseEntity<LoginModel> response = loginController.getLoginByRut("12345678-9");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(login, response.getBody());
        verify(loginService, times(1)).buscarLoginPorRut("12345678-9");
    }

    @Test
    void testGetLoginByRut_NotFound() {
        when(loginService.buscarLoginPorRut("99999999-9")).thenReturn(Optional.empty());

        ResponseEntity<LoginModel> response = loginController.getLoginByRut("99999999-9");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(loginService, times(1)).buscarLoginPorRut("99999999-9");
    }

    @Test
    void testCreateLogin() {
        LoginModel login = crearLoginEjemplo();
        when(loginService.guardarLogin(login)).thenReturn(login);

        ResponseEntity<LoginModel> response = loginController.createLogin(login);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(login, response.getBody());
        verify(loginService, times(1)).guardarLogin(login);
    }

    @Test
    void testUpdateLogin_Found() {
        LoginModel login = crearLoginEjemplo();
        when(loginService.actualizarLogin(login)).thenReturn(login);

        ResponseEntity<LoginModel> response = loginController.updateLogin(login);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(login, response.getBody());
        verify(loginService, times(1)).actualizarLogin(login);
    }

    @Test
    void testUpdateLogin_NotFound() {
        LoginModel login = crearLoginEjemplo();
        when(loginService.actualizarLogin(login)).thenReturn(null);

        ResponseEntity<LoginModel> response = loginController.updateLogin(login);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(loginService, times(1)).actualizarLogin(login);
    }

    @Test
    void testDeleteLogin_Found() {
        when(loginService.eliminarLogin("12345678-9")).thenReturn(true);

        ResponseEntity<Void> response = loginController.deleteLogin("12345678-9");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(loginService, times(1)).eliminarLogin("12345678-9");
    }

    @Test
    void testDeleteLogin_NotFound() {
        when(loginService.eliminarLogin("99999999-9")).thenReturn(false);

        ResponseEntity<Void> response = loginController.deleteLogin("99999999-9");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(loginService, times(1)).eliminarLogin("99999999-9");
    }
}
