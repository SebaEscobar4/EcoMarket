package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.LogisticaModel;
import com.example.EcoMarket.service.LogisticaService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/logistica")
public class LogisticaController {

    private final LogisticaService logisticaService;

    public LogisticaController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<LogisticaModel>>> obtenerTodas() {
        List<EntityModel<LogisticaModel>> logisticaList = logisticaService.obtenerTodas().stream()
                .map(logistica -> EntityModel.of(logistica,
                        linkTo(methodOn(LogisticaController.class).obtenerPorId(logistica.getId())).withSelfRel(),
                        linkTo(methodOn(LogisticaController.class).obtenerTodas()).withRel("todas")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(logisticaList,
                linkTo(methodOn(LogisticaController.class).obtenerTodas()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<LogisticaModel>> obtenerPorId(@PathVariable int id) {
        Optional<LogisticaModel> logistica = logisticaService.buscarPorId(id);

        return logistica.map(l -> EntityModel.of(l,
                        linkTo(methodOn(LogisticaController.class).obtenerPorId(id)).withSelfRel(),
                        linkTo(methodOn(LogisticaController.class).obtenerTodas()).withRel("todas"),
                        linkTo(methodOn(LogisticaController.class).eliminar(id)).withRel("eliminar"),
                        linkTo(methodOn(LogisticaController.class).actualizar(id, l)).withRel("actualizar")))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<LogisticaModel>> crear(@RequestBody LogisticaModel logistica) {
        LogisticaModel guardada = logisticaService.guardar(logistica);

        EntityModel<LogisticaModel> resource = EntityModel.of(guardada,
                linkTo(methodOn(LogisticaController.class).obtenerPorId(guardada.getId())).withSelfRel(),
                linkTo(methodOn(LogisticaController.class).obtenerTodas()).withRel("todas"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<LogisticaModel>> actualizar(@PathVariable int id, @RequestBody LogisticaModel logistica) {
        if (id != logistica.getId()) {
            return ResponseEntity.badRequest().build();
        }
        LogisticaModel actualizada = logisticaService.actualizar(logistica);
        if (actualizada != null) {
            EntityModel<LogisticaModel> resource = EntityModel.of(actualizada,
                    linkTo(methodOn(LogisticaController.class).obtenerPorId(actualizada.getId())).withSelfRel(),
                    linkTo(methodOn(LogisticaController.class).obtenerTodas()).withRel("todas"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        boolean eliminado = logisticaService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
