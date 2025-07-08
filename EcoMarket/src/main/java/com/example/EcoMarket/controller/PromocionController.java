package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.PromocionModel;
import com.example.EcoMarket.service.PromocionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/promociones")
@Tag(name = "Promociones", description = "API para gestionar promociones")
public class PromocionController {

    private final PromocionService promocionService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las promociones")
    public ResponseEntity<List<PromocionModel>> obtenerTodas() {
        return ResponseEntity.ok(promocionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener promoción por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Promoción encontrada"),
                    @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
            }
    )
    public ResponseEntity<PromocionModel> obtenerPorId(
            @Parameter(description = "ID de la promoción", required = true)
            @PathVariable int id) {
        Optional<PromocionModel> promocion = promocionService.buscarPorId(id);
        return promocion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva promoción")
    public ResponseEntity<PromocionModel> crear(
            @Parameter(description = "Objeto promoción a crear", required = true)
            @RequestBody PromocionModel promocion) {
        return new ResponseEntity<>(promocionService.guardar(promocion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una promoción existente")
    public ResponseEntity<PromocionModel> actualizar(
            @Parameter(description = "ID de la promoción a actualizar", required = true)
            @PathVariable int id,
            @Parameter(description = "Datos actualizados de la promoción", required = true)
            @RequestBody PromocionModel promocion) {
        if (id != promocion.getId()) {
            return ResponseEntity.badRequest().build();
        }
        PromocionModel actualizada = promocionService.actualizar(promocion);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una promoción por ID")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la promoción a eliminar", required = true)
            @PathVariable int id) {
        boolean eliminado = promocionService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}