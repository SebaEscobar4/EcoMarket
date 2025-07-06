package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.InventarioModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InventarioRepositoryTest {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Test
    @DisplayName("Guardar y buscar producto por nombre")
    void testFindByNombreProducto() {
        // Arrange
        InventarioModel producto = new InventarioModel(
                "P001",
                "Aceite de Oliva",
                LocalDate.of(2025, 12, 31),
                "Chile"
        );

        inventarioRepository.save(producto);

        // Act
        Optional<InventarioModel> resultado = inventarioRepository.findByNombreProducto("Aceite de Oliva");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo("P001");
        assertThat(resultado.get().getPaisCreacion()).isEqualTo("Chile");
    }

    @Test
    @DisplayName("Buscar producto inexistente por nombre")
    void testFindByNombreProductoNoExistente() {
        Optional<InventarioModel> resultado = inventarioRepository.findByNombreProducto("Producto Fantasma");

        assertThat(resultado).isNotPresent();
    }
}
