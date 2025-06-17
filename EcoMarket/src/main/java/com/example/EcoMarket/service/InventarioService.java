package com.example.EcoMarket.service;

import com.example.EcoMarket.model.InventarioModel;
import com.example.EcoMarket.repository.InventarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<InventarioModel> obtenerTodosLosProductos() {
        return inventarioRepository.findAll();
    }

    public Optional<InventarioModel> buscarPorId(String id) {
        return inventarioRepository.findById(id);
    }

    public InventarioModel guardarProducto(InventarioModel inventario) {
        return inventarioRepository.save(inventario);
    }

    public InventarioModel actualizarProducto(InventarioModel inventario) {
        Optional<InventarioModel> existingProductoOptional = inventarioRepository.findById(inventario.getId());

        if (existingProductoOptional.isPresent()) {
            InventarioModel existingProducto = existingProductoOptional.get();
            existingProducto.setNombreProducto(inventario.getNombreProducto());
            existingProducto.setFechaCad(inventario.getFechaCad());
            existingProducto.setPaisCreacion(inventario.getPaisCreacion());

            return inventarioRepository.save(existingProducto);
        } else {
            return null;
        }
    }

    public boolean eliminarProducto(String id) {
        if (inventarioRepository.existsById(id)) {
            inventarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
