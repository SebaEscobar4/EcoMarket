package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.TiendaModel;
import com.example.EcoMarket.service.TiendaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

public class TiendaControllerTest {

    @Mock
    private TiendaService tiendaService;

    @InjectMocks
    private TiendaController tiendaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTiendas() {
        // Arrange
        TiendaModel t1 = new TiendaModel("1", "Tienda A", "Calle A 123", "123456789", "a@tienda.com");
        TiendaModel t2 = new TiendaModel("2", "Tienda B", "Calle B 456", "987654321", "b@tienda.com");

        when(tiendaService.obtenerTodasLasTiendas()).thenReturn(Arrays.asList(t1, t2));

        // Act
        ResponseEntity<CollectionModel<EntityModel<TiendaModel>>> response = tiendaController.getAllTiendas();

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        List<EntityModel<TiendaModel>> listaTiendas = response.getBody().getContent().stream().toList();
        assertThat(listaTiendas).hasSize(2);

        assertThat(listaTiendas.get(0).getContent().getNombre()).isEqualTo("Tienda A");
        assertThat(listaTiendas.get(0).getContent().getDireccion()).isEqualTo("Calle A 123");
        assertThat(listaTiendas.get(0).getContent().getTelefono()).isEqualTo("123456789");
        assertThat(listaTiendas.get(0).getContent().getCorreo()).isEqualTo("a@tienda.com");

        assertThat(listaTiendas.get(1).getContent().getNombre()).isEqualTo("Tienda B");
        assertThat(listaTiendas.get(1).getContent().getDireccion()).isEqualTo("Calle B 456");
        assertThat(listaTiendas.get(1).getContent().getTelefono()).isEqualTo("987654321");
        assertThat(listaTiendas.get(1).getContent().getCorreo()).isEqualTo("b@tienda.com");

        // Verificar que los links existan
        assertThat(listaTiendas.get(0).getLink("self")).isPresent();
        assertThat(listaTiendas.get(1).getLink("self")).isPresent();

        assertThat(response.getBody().getLink("self")).isPresent();

        verify(tiendaService, times(1)).obtenerTodasLasTiendas();
    }

    @Test
    void testGetTiendaByIdFound() {
        // Arrange
        TiendaModel t1 = new TiendaModel("1", "Tienda A", "Calle A 123", "123456789", "a@tienda.com");
        when(tiendaService.buscarTiendaPorId("1")).thenReturn(Optional.of(t1));

        // Act
        ResponseEntity<EntityModel<TiendaModel>> response = tiendaController.getTiendaById("1");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        EntityModel<TiendaModel> recurso = response.getBody();

        assertThat(recurso.getContent().getNombre()).isEqualTo("Tienda A");
        assertThat(recurso.getContent().getDireccion()).isEqualTo("Calle A 123");
        assertThat(recurso.getContent().getTelefono()).isEqualTo("123456789");
        assertThat(recurso.getContent().getCorreo()).isEqualTo("a@tienda.com");

        assertThat(recurso.getLink("self")).isPresent();
        assertThat(recurso.getLink("tiendas")).isPresent();

        verify(tiendaService, times(1)).buscarTiendaPorId("1");
    }

    @Test
    void testGetTiendaByIdNotFound() {
        // Arrange
        when(tiendaService.buscarTiendaPorId("99")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<EntityModel<TiendaModel>> response = tiendaController.getTiendaById("99");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        verify(tiendaService, times(1)).buscarTiendaPorId("99");
    }

    @Test
    void testCreateTienda() {
        // Arrange
        TiendaModel input = new TiendaModel("1", "Tienda A", "Calle A 123", "123456789", "a@tienda.com");
        TiendaModel saved = new TiendaModel("1", "Tienda A", "Calle A 123", "123456789", "a@tienda.com");

        when(tiendaService.guardarTienda(input)).thenReturn(saved);

        // Act
        ResponseEntity<EntityModel<TiendaModel>> response = tiendaController.createTienda(input);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().getContent().getNombre()).isEqualTo("Tienda A");
        assertThat(response.getBody().getContent().getDireccion()).isEqualTo("Calle A 123");
        assertThat(response.getBody().getContent().getTelefono()).isEqualTo("123456789");
        assertThat(response.getBody().getContent().getCorreo()).isEqualTo("a@tienda.com");
        assertThat(response.getBody().getLink("self")).isPresent();

        verify(tiendaService, times(1)).guardarTienda(input);
    }

    @Test
    void testUpdateTiendaFound() {
        // Arrange
        TiendaModel input = new TiendaModel("1", "Tienda A", "Calle A 123", "123456789", "a@tienda.com");
        TiendaModel updated = new TiendaModel("1", "Tienda A Actualizada", "Calle A 123", "123456789", "a@tienda.com");

        when(tiendaService.actualizarTienda(input)).thenReturn(updated);

        // Act
        ResponseEntity<EntityModel<TiendaModel>> response = tiendaController.updateTienda(input);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getContent().getNombre()).isEqualTo("Tienda A Actualizada");
        assertThat(response.getBody().getLink("self")).isPresent();

        verify(tiendaService, times(1)).actualizarTienda(input);
    }

    @Test
    void testUpdateTiendaNotFound() {
        // Arrange
        TiendaModel input = new TiendaModel("99", "Tienda X", "Calle X 123", "111111111", "x@tienda.com");

        when(tiendaService.actualizarTienda(input)).thenReturn(null);

        // Act
        ResponseEntity<EntityModel<TiendaModel>> response = tiendaController.updateTienda(input);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        verify(tiendaService, times(1)).actualizarTienda(input);
    }

    @Test
    void testDeleteTiendaSuccess() {
        // Arrange
        when(tiendaService.eliminarTienda("1")).thenReturn(true);

        // Act
        ResponseEntity<Void> response = tiendaController.deleteTienda("1");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(tiendaService, times(1)).eliminarTienda("1");
    }

    @Test
    void testDeleteTiendaNotFound() {
        // Arrange
        when(tiendaService.eliminarTienda("99")).thenReturn(false);

        // Act
        ResponseEntity<Void> response = tiendaController.deleteTienda("99");

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        verify(tiendaService, times(1)).eliminarTienda("99");
    }
}
