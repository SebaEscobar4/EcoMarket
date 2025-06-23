package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.InventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioModel, String> {

    Optional<InventarioModel> findByNombreProducto(String nombreProducto);
}
