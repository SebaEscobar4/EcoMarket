package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.PromocionModel;
import com.example.EcoMarket.service.PromocionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/promociones")
public class PromocionController {

    private final PromocionService promocionService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping
    public ResponseEntity<List<PromocionModel>> obtenerTodas() {
        return ResponseEntity.ok(promocionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromocionModel> obtenerPorId(@PathVariable int id) {
        Optional<PromocionModel> promocion = promocionService.buscarPorId(id);
        return promocion.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PromocionModel> crear(@RequestBody PromocionModel promocion) {
        return new ResponseEntity<>(promocionService.guardar(promocion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromocionModel> actualizar(@PathVariable int id, @RequestBody PromocionModel promocion) {
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
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        boolean eliminado = promocionService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

