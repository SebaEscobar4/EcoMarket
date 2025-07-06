package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.InformeModel;
import com.example.EcoMarket.service.InformeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/informes")
@Tag(name = "Informes", description = "Operaciones relacionadas con informes")
public class InformeController {

    private final InformeService informeService;

    public InformeController(InformeService informeService) {
        this.informeService = informeService;
    }

    @Operation(summary = "Obtener todos los informes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de informes obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<InformeModel>> obtenerTodos() {
        List<InformeModel> informes = informeService.obtenerTodosLosInformes();
        return ResponseEntity.ok(informes);
    }

    @Operation(summary = "Obtener un informe por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informe encontrado"),
            @ApiResponse(responseCode = "404", description = "Informe no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InformeModel> obtenerPorId(@PathVariable String id) {
        Optional<InformeModel> informeOpt = informeService.buscarPorId(id);
        return informeOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo informe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Informe creado exitosamente")
    })
    @PostMapping
    public ResponseEntity<InformeModel> crearInforme(@RequestBody InformeModel informe) {
        InformeModel nuevoInforme = informeService.guardarInforme(informe);
        return new ResponseEntity<>(nuevoInforme, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un informe existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informe actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID del informe no coincide"),
            @ApiResponse(responseCode = "404", description = "Informe no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InformeModel> actualizarInforme(@PathVariable String id,
                                                          @RequestBody InformeModel informe) {
        if (!id.equals(informe.getId())) {
            return ResponseEntity.badRequest().build();
        }

        InformeModel actualizado = informeService.actualizarInforme(informe);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar un informe por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Informe eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Informe no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInforme(@PathVariable String id) {
        boolean eliminado = informeService.eliminarInforme(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
