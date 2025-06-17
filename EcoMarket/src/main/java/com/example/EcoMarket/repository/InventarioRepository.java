package com.example.EcoMarket.repository;


import com.example.EcoMarket.model.InventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*importacion de Java*/
import java.util.Optional; 

@Repository
/*Hacemos una extencion para que obtenga todos los metodos del CRUD */
public interface InventarioRepository extends JpaRepository<InventarioModel, String> {

    Optional<InventarioModel> findByRut(String id);

}