package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.InformeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformeRepository extends JpaRepository<InformeModel, String> {
}
