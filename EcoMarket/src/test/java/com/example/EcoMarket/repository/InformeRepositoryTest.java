package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.InformeModel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class InformeRepositoryTest {

    @Autowired
    private InformeRepository informeRepository;

    @Test
    public void testGuardarYBuscarInforme() {
        // Arrange
        InformeModel informe = new InformeModel(
                "123",
                "Informe de pruebas",
                "Este es un informe de prueba",
                LocalDate.of(2025, 7, 6),
                "Benjamin"
        );

        // Act
        informeRepository.save(informe);
        Optional<InformeModel> resultado = informeRepository.findById("123");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Informe de pruebas");
        assertThat(resultado.get().getDescripcion()).isEqualTo("Este es un informe de prueba");
    }

    @Test
    public void testEliminarInforme() {
        // Arrange
        InformeModel informe = new InformeModel("456", "Eliminar", "Se eliminará", LocalDate.now(), "Benja");
        informeRepository.save(informe);

        // Act
        informeRepository.deleteById("456");
        Optional<InformeModel> eliminado = informeRepository.findById("456");

        // Assert
        assertThat(eliminado).isNotPresent();
    }
}
