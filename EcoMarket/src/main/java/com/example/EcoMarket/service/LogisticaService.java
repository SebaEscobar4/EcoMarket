package com.example.EcoMarket.service;

import com.example.EcoMarket.model.LogisticaModel;
import com.example.EcoMarket.repository.LogisticaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogisticaService {

    private final LogisticaRepository logisticaRepository;

    public LogisticaService(LogisticaRepository logisticaRepository) {
        this.logisticaRepository = logisticaRepository;
    }

    public List<LogisticaModel> obtenerTodas() {
        return logisticaRepository.findAll();
    }

    public Optional<LogisticaModel> buscarPorId(int id) {
        return logisticaRepository.findById(id);
    }

    public LogisticaModel guardar(LogisticaModel logistica) {
        return logisticaRepository.save(logistica);
    }

    public LogisticaModel actualizar(LogisticaModel logistica) {
        if (logisticaRepository.existsById(logistica.getId())) {
            return logisticaRepository.save(logistica);
        }
        return null;
    }

    public boolean eliminar(int id) {
        if (logisticaRepository.existsById(id)) {
            logisticaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
