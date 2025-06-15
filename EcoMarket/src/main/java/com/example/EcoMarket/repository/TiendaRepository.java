package com.example.EcoMarket.repository;
import com.example.EcoMarket.model.TiendaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface  TiendaRepository extends JpaRepository<TiendaModel, String> {

    Optional <TiendaModel> findByRut(String id);
    

}
