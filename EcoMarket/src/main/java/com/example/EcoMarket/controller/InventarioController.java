package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.InventarioModel;
import com.example.EcoMarket.service.InventarioService;

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
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Operaciones relacionadas con el inventario de productos")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(summary = "Obtener todos los productos del inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente")
    })
    @GetMapping
    public ResponseEntity<List<InventarioModel>> getAllInventario() {
        List<InventarioModel> inventario = inventarioService.obtenerTodosLosProductos();
        return ResponseEntity.ok(inventario);
    }

    @Operation(summary = "Obtener un producto del inventario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventarioModel> getInventarioById(@PathVariable String id) {
        return inventarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Crear un nuevo producto en el inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente")
    })
    @PostMapping
    public ResponseEntity<InventarioModel> createProducto(@RequestBody InventarioModel producto) {
        InventarioModel savedProducto = inventarioService.guardarProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProducto);
    }

    @Operation(summary = "Actualizar un producto existente en el inventario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventarioModel> updateProducto(@PathVariable String id, @RequestBody InventarioModel producto) {
        producto.setId(id); // Asegura que el ID en la URL prevalezca
        InventarioModel updatedProducto = inventarioService.actualizarProducto(producto);
        if (updatedProducto != null) {
            return ResponseEntity.ok(updatedProducto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar un producto del inventario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable String id) {
        boolean deleted = inventarioService.eliminarProducto(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
