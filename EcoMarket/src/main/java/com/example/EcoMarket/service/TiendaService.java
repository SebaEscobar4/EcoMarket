package com.example.EcoMarket.service;

import com.example.EcoMarket.model.TiendaModel;
import com.example.EcoMarket.repository.TiendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TiendaService {

    private final TiendaRepository tiendaRepository;


    public TiendaService(TiendaRepository tiendaRepository) {
        this.tiendaRepository = tiendaRepository;
    }

    public List<TiendaModel> obtenerTodasLasTiendas() {
        return tiendaRepository.findAll(); 
    }

    public Optional<TiendaModel> buscarTiendaPorId(String id) {
        return tiendaRepository.findById(id);
    }

    public TiendaModel guardarTienda(TiendaModel tienda) {
        return tiendaRepository.save(tienda);
    }

    public TiendaModel actualizarTienda(TiendaModel tienda) {
        Optional<TiendaModel> existingTiendaOptional = tiendaRepository.findById(tienda.getId());

        if (existingTiendaOptional.isPresent()) {
            TiendaModel existingTienda = existingTiendaOptional.get();
            existingTienda.setNombre(tienda.getNombre());
            existingTienda.setDireccion(tienda.getDireccion());
            existingTienda.setTelefono(tienda.getTelefono());
            existingTienda.setCorreo(tienda.getCorreo());
        


            return tiendaRepository.save(existingTienda);
        } else {
            return null;
        }
    }

    public boolean eliminarTienda(String id) {
        if (tiendaRepository.existsById(id)) {
            tiendaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
