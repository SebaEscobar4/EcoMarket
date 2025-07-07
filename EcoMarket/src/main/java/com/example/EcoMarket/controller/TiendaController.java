package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.TiendaModel;
import com.example.EcoMarket.service.TiendaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tiendas")
public class TiendaController {

    private final TiendaService tiendaService;

    public TiendaController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping
    public ResponseEntity<List<TiendaModel>> getAllTiendas() {
        List<TiendaModel> tiendas = tiendaService.obtenerTodasLasTiendas();
        return new ResponseEntity<>(tiendas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TiendaModel> getTiendaById(@PathVariable String id) {
        Optional<TiendaModel> tienda = tiendaService.buscarTiendaPorId(id);
        return tienda.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<TiendaModel> createTienda(@RequestBody TiendaModel tienda) {
        TiendaModel savedTienda = tiendaService.guardarTienda(tienda);
        return new ResponseEntity<>(savedTienda, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TiendaModel> updateTienda(@RequestBody TiendaModel tienda) {
        TiendaModel updatedTienda = tiendaService.actualizarTienda(tienda);
        if (updatedTienda != null) {
            return new ResponseEntity<>(updatedTienda, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

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