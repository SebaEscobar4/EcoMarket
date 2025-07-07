package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.TiendaModel;
import com.example.EcoMarket.service.TiendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tiendas")
@Tag(name = "Tienda", description = "Operaciones relacionadas con las tiendas")
public class TiendaController {

    private final TiendaService tiendaService;

    public TiendaController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @Operation(summary = "Obtener todas las tiendas")
    @ApiResponse(responseCode = "200", description = "Tiendas encontradas exitosamente")
    @GetMapping
    public ResponseEntity<List<TiendaModel>> getAllTiendas() {
        List<TiendaModel> tiendas = tiendaService.obtenerTodasLasTiendas();
        return new ResponseEntity<>(tiendas, HttpStatus.OK);
    }

    @Operation(summary = "Obtener una tienda por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tienda encontrada"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TiendaModel> getTiendaById(@PathVariable String id) {
        Optional<TiendaModel> tienda = tiendaService.buscarTiendaPorId(id);
        return tienda.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear una nueva tienda")
    @ApiResponse(responseCode = "201", description = "Tienda creada exitosamente")
    @PostMapping
    public ResponseEntity<TiendaModel> createTienda(@RequestBody TiendaModel tienda) {
        TiendaModel savedTienda = tiendaService.guardarTienda(tienda);
        return new ResponseEntity<>(savedTienda, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una tienda existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tienda actualizada"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada")
    })
    @PutMapping
    public ResponseEntity<TiendaModel> updateTienda(@RequestBody TiendaModel tienda) {
        TiendaModel updatedTienda = tiendaService.actualizarTienda(tienda);
        if (updatedTienda != null) {
            return new ResponseEntity<>(updatedTienda, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar una tienda por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tienda eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTienda(@PathVariable String id) {
        boolean deleted = tiendaService.eliminarTienda(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
