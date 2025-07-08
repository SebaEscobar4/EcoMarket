package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.LoginModel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LoginRepositoryTest {

    @Autowired
    private LoginRepository loginRepository;

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
    @DisplayName("Guardar y buscar login por RUT")
    void testGuardarYBuscarPorRut() {
        // Arrange
        LoginModel login = crearLoginEjemplo();
        loginRepository.save(login);

        // Act
        Optional<LoginModel> resultado = loginRepository.findByRut("12345678-9");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombreP());
        assertEquals("juan@example.com", resultado.get().getCorreoElectronico());
    }

    @Test
    @DisplayName("Buscar login inexistente por RUT")
    void testBuscarRutInexistente() {
        Optional<LoginModel> resultado = loginRepository.findByRut("99999999-9");
        assertFalse(resultado.isPresent());
    }
}
