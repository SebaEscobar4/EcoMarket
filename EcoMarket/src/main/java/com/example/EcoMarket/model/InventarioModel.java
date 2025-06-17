package com.example.EcoMarket.model; 
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Inventario" )



public class InventarioModel {
    @Id
    private String id;
    private String nombreProducto;
    private int fechaCad;
    private String paisCreacion;
}