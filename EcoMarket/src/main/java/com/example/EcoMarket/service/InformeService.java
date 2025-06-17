package com.example.EcoMarket.service;

import com.example.EcoMarket.model.InformeModel;
import com.example.EcoMarket.repository.InformeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InformeService {

    private final InformeRepository informeRepository;

    public InformeService(InformeRepository informeRepository) {
        this.informeRepository = informeRepository;
    }

    public List<InformeModel> obtenerTodosLosInformes() {
        return informeRepository.findAll();
    }

    public Optional<InformeModel> buscarPorId(String id) {
        return informeRepository.findById(id);
    }

    public InformeModel guardarInforme(InformeModel informe) {
        return informeRepository.save(informe);
    }

    public InformeModel actualizarInforme(InformeModel informe) {
        Optional<InformeModel> existingInformeOptional = informeRepository.findById(informe.getId());
        if (existingInformeOptional.isPresent()) {
            InformeModel existingInforme = existingInformeOptional.get();

            existingInforme.setTitulo(informe.getTitulo());
            existingInforme.setDescripcion(informe.getDescripcion());
            existingInforme.setFechaCreacion(informe.getFechaCreacion());
            existingInforme.setAutor(informe.getAutor());

            return informeRepository.save(existingInforme);
        } else {
            return null;
        }
    }

    public boolean eliminarInforme(String id) {
        if (informeRepository.existsById(id)) {
            informeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
