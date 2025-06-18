package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.LogisticaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticaRepository extends JpaRepository<LogisticaModel, Integer> {
}