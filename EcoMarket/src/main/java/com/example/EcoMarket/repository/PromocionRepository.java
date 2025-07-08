package com.example.EcoMarket.repository;

import com.example.EcoMarket.model.PromocionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocionRepository extends JpaRepository<PromocionModel, Integer> {

}