package com.example.EcoMarket.model; 
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Inventario" )



public class InventarioModel {
    @Id
    private String id;
    private String nombreProducto;
    private LocalDate fechaCad;
    private String paisCreacion;
}