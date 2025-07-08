package com.example.EcoMarket.service;

import com.example.EcoMarket.model.LoginModel;
import com.example.EcoMarket.repository.LoginRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    private LoginRepository loginRepository;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        loginRepository = mock(LoginRepository.class);
        loginService = new LoginService(loginRepository);
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
    void testObtenerTodosLosLogins() {
        List<LoginModel> logins = Arrays.asList(crearLoginEjemplo());
        when(loginRepository.findAll()).thenReturn(logins);

        List<LoginModel> resultado = loginService.obtenerTodosLosLogins();

        assertEquals(1, resultado.size());
        verify(loginRepository, times(1)).findAll();
    }

    @Test
    void testBuscarLoginPorRut_Existe() {
        LoginModel login = crearLoginEjemplo();
        when(loginRepository.findByRut("12345678-9")).thenReturn(Optional.of(login));

        Optional<LoginModel> resultado = loginService.buscarLoginPorRut("12345678-9");

        assertTrue(resultado.isPresent());
        assertEquals(login, resultado.get());
        verify(loginRepository, times(1)).findByRut("12345678-9");
    }

    @Test
    void testBuscarLoginPorRut_NoExiste() {
        when(loginRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        Optional<LoginModel> resultado = loginService.buscarLoginPorRut("99999999-9");

        assertFalse(resultado.isPresent());
        verify(loginRepository, times(1)).findByRut("99999999-9");
    }

    @Test
    void testGuardarLogin() {
        LoginModel login = crearLoginEjemplo();
        when(loginRepository.save(login)).thenReturn(login);

        LoginModel resultado = loginService.guardarLogin(login);

        assertEquals(login, resultado);
        verify(loginRepository, times(1)).save(login);
    }

    @Test
    void testActualizarLogin_Existe() {
        LoginModel login = crearLoginEjemplo();
        when(loginRepository.findByRut(login.getRut())).thenReturn(Optional.of(login));
        when(loginRepository.save(any(LoginModel.class))).thenReturn(login);

        LoginModel actualizado = loginService.actualizarLogin(login);

        assertNotNull(actualizado);
        assertEquals(login.getRut(), actualizado.getRut());
        verify(loginRepository).save(any(LoginModel.class));
    }

    @Test
    void testActualizarLogin_NoExiste() {
        LoginModel login = crearLoginEjemplo();
        when(loginRepository.findByRut(login.getRut())).thenReturn(Optional.empty());

        LoginModel actualizado = loginService.actualizarLogin(login);

        assertNull(actualizado);
        verify(loginRepository, never()).save(any(LoginModel.class));
    }

    @Test
    void testEliminarLogin_Existe() {
        when(loginRepository.existsById("12345678-9")).thenReturn(true);

        boolean eliminado = loginService.eliminarLogin("12345678-9");

        assertTrue(eliminado);
        verify(loginRepository).deleteById("12345678-9");
    }

    @Test
    void testEliminarLogin_NoExiste() {
        when(loginRepository.existsById("99999999-9")).thenReturn(false);

        boolean eliminado = loginService.eliminarLogin("99999999-9");

        assertFalse(eliminado);
        verify(loginRepository, never()).deleteById(any());
    }
}
