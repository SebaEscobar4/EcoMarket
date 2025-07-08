package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.TiendaModel;
import com.example.EcoMarket.service.TiendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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
    public ResponseEntity<CollectionModel<EntityModel<TiendaModel>>> getAllTiendas() {
        List<EntityModel<TiendaModel>> tiendas = tiendaService.obtenerTodasLasTiendas()
                .stream()
                .map(tienda -> EntityModel.of(tienda,
                        linkTo(methodOn(TiendaController.class).getTiendaById(tienda.getId())).withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<TiendaModel>> coleccion = CollectionModel.of(tiendas,
                linkTo(methodOn(TiendaController.class).getAllTiendas()).withSelfRel());

        return new ResponseEntity<>(coleccion, HttpStatus.OK);
    }

    @Operation(summary = "Obtener una tienda por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tienda encontrada"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<TiendaModel>> getTiendaById(@PathVariable String id) {
        Optional<TiendaModel> tienda = tiendaService.buscarTiendaPorId(id);

        return tienda.map(value -> {
            EntityModel<TiendaModel> recurso = EntityModel.of(value,
                    linkTo(methodOn(TiendaController.class).getTiendaById(id)).withSelfRel(),
                    linkTo(methodOn(TiendaController.class).getAllTiendas()).withRel("tiendas"));
            return new ResponseEntity<>(recurso, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear una nueva tienda")
    @ApiResponse(responseCode = "201", description = "Tienda creada exitosamente")
    @PostMapping
    public ResponseEntity<EntityModel<TiendaModel>> createTienda(@RequestBody TiendaModel tienda) {
        TiendaModel savedTienda = tiendaService.guardarTienda(tienda);
        EntityModel<TiendaModel> recurso = EntityModel.of(savedTienda,
                linkTo(methodOn(TiendaController.class).getTiendaById(savedTienda.getId())).withSelfRel(),
                linkTo(methodOn(TiendaController.class).getAllTiendas()).withRel("tiendas"));

        return new ResponseEntity<>(recurso, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una tienda existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tienda actualizada"),
            @ApiResponse(responseCode = "404", description = "Tienda no encontrada")
    })
    @PutMapping
    public ResponseEntity<EntityModel<TiendaModel>> updateTienda(@RequestBody TiendaModel tienda) {
        TiendaModel updatedTienda = tiendaService.actualizarTienda(tienda);
        if (updatedTienda != null) {
            EntityModel<TiendaModel> recurso = EntityModel.of(updatedTienda,
                    linkTo(methodOn(TiendaController.class).getTiendaById(updatedTienda.getId())).withSelfRel(),
                    linkTo(methodOn(TiendaController.class).getAllTiendas()).withRel("tiendas"));
            return new ResponseEntity<>(recurso, HttpStatus.OK);
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
