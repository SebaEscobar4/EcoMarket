package com.example.EcoMarket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Informe")
public class InformeModel {
    
    @Id
    private String id;

    private String titulo;
    private String descripcion;
    private LocalDate fechaCreacion;
    private String autor; 
}