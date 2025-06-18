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
@Table(name = "Logistica")
public class LogisticaModel {

    @Id
   
    private int id;

    private String tipoTransporte; // Ej: terrestre, aéreo, marítimo
    private String destino;
    private LocalDate fechaEnvio;
    private LocalDate fechaEntrega;
    private String encargado;
}





