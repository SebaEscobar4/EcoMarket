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
@Table(name = "Tienda" )



public class TiendaModel {
    @Id
    private String id;

    private String nombre;
    private String Direccion;
    private String telefono;
    private String correo;
    


}
