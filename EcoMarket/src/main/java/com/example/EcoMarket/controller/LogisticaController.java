package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.LogisticaModel;
import com.example.EcoMarket.service.LogisticaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/logistica")
public class LogisticaController {

    private final LogisticaService logisticaService;

    public LogisticaController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    @GetMapping
    public ResponseEntity<List<LogisticaModel>> obtenerTodas() {
        return ResponseEntity.ok(logisticaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogisticaModel> obtenerPorId(@PathVariable int id) {
        Optional<LogisticaModel> logistica = logisticaService.buscarPorId(id);
        return logistica.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LogisticaModel> crear(@RequestBody LogisticaModel logistica) {
        return new ResponseEntity<>(logisticaService.guardar(logistica), HttpStatus.CREATED);
    }
@PutMapping("/{id}")
    public ResponseEntity<LogisticaModel> actualizar(@PathVariable int id, @RequestBody LogisticaModel logistica) {
        if (id != logistica.getId()) {
            return ResponseEntity.badRequest().build();
        }
        LogisticaModel actualizada = logisticaService.actualizar(logistica);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        boolean eliminado = logisticaService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}