package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.PromocionModel;
import com.example.EcoMarket.service.PromocionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<CollectionModel<EntityModel<PromocionModel>>> obtenerTodas() {
        List<EntityModel<PromocionModel>> promociones = promocionService.obtenerTodas().stream()
                .map(promocion -> EntityModel.of(promocion,
                        linkTo(methodOn(PromocionController.class).obtenerPorId(promocion.getId())).withSelfRel(),
                        linkTo(methodOn(PromocionController.class).obtenerTodas()).withRel("todas")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(promociones, linkTo(methodOn(PromocionController.class).obtenerTodas()).withSelfRel())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener promoción por ID")
    public ResponseEntity<EntityModel<PromocionModel>> obtenerPorId(
            @Parameter(description = "ID de la promoción", required = true)
            @PathVariable int id) {
        Optional<PromocionModel> promocion = promocionService.buscarPorId(id);
        return promocion.map(p -> EntityModel.of(p,
                        linkTo(methodOn(PromocionController.class).obtenerPorId(id)).withSelfRel(),
                        linkTo(methodOn(PromocionController.class).obtenerTodas()).withRel("todas"),
                        linkTo(methodOn(PromocionController.class).eliminar(id)).withRel("eliminar"),
                        linkTo(methodOn(PromocionController.class).actualizar(id, p)).withRel("actualizar")))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva promoción")
    public ResponseEntity<EntityModel<PromocionModel>> crear(
            @Parameter(description = "Objeto promoción a crear", required = true)
            @RequestBody PromocionModel promocion) {
        PromocionModel guardada = promocionService.guardar(promocion);
        EntityModel<PromocionModel> resource = EntityModel.of(guardada,
                linkTo(methodOn(PromocionController.class).obtenerPorId(guardada.getId())).withSelfRel(),
                linkTo(methodOn(PromocionController.class).obtenerTodas()).withRel("todas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una promoción existente")
    public ResponseEntity<EntityModel<PromocionModel>> actualizar(
            @PathVariable int id,
            @RequestBody PromocionModel promocion) {
        if (id != promocion.getId()) {
            return ResponseEntity.badRequest().build();
        }
        PromocionModel actualizada = promocionService.actualizar(promocion);
        if (actualizada != null) {
            EntityModel<PromocionModel> resource = EntityModel.of(actualizada,
                    linkTo(methodOn(PromocionController.class).obtenerPorId(actualizada.getId())).withSelfRel(),
                    linkTo(methodOn(PromocionController.class).obtenerTodas()).withRel("todas"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una promoción por ID")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        boolean eliminado = promocionService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
