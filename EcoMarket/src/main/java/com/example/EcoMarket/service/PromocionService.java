package com.example.EcoMarket.service;

import com.example.EcoMarket.model.PromocionModel;
import com.example.EcoMarket.repository.PromocionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromocionService {

    private final PromocionRepository promocionRepository;

    public PromocionService(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    public List<PromocionModel> obtenerTodas() {
        return promocionRepository.findAll();
    }

    public Optional<PromocionModel> buscarPorId(int id) {
        return promocionRepository.findById(id);
    }

    public PromocionModel guardar(PromocionModel promocion) {
        return promocionRepository.save(promocion);
    }

    public PromocionModel actualizar(PromocionModel promocion) {
        if (promocionRepository.existsById(promocion.getId())) {
            return promocionRepository.save(promocion);
        }
        return null;
    }

    public boolean eliminar(int id) {
        if (promocionRepository.existsById(id)) {
            promocionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}