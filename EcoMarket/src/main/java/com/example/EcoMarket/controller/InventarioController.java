package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.InventarioModel;
import com.example.EcoMarket.service.InventarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final InventarioService inventarioService;



    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<InventarioModel>> getAllInventario() {
        List<InventarioModel> inventario = inventarioService.obtenerTodosLosProductos();
        return new ResponseEntity<>(inventario, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<InventarioModel> getInventarioByRut(@PathVariable String id) {
        Optional<InventarioModel> inventario = inventarioService.buscarPorId(id);
        return inventario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public ResponseEntity<InventarioModel> createProducto(@RequestBody InventarioModel producto) {
        InventarioModel savedProducto = inventarioService.guardarProducto(producto);
        return new ResponseEntity<>(savedProducto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<InventarioModel> updateProducto(@RequestBody InventarioModel producto) {
        InventarioModel updatedproducto = inventarioService.actualizarProducto(producto);
        if (updatedproducto != null) {
            return new ResponseEntity<>(updatedproducto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable String id) {
        boolean deleted = inventarioService.eliminarProducto(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}