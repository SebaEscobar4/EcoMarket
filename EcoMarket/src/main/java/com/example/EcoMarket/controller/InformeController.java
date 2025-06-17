package com.example.EcoMarket.controller;

import com.example.EcoMarket.model.InformeModel;
import com.example.EcoMarket.service.InformeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/informes")
public class InformeController {

    private final InformeService informeService;

    public InformeController(InformeService informeService) {
        this.informeService = informeService;
    }

    @GetMapping
    public ResponseEntity<List<InformeModel>> obtenerTodos() {
        List<InformeModel> informes = informeService.obtenerTodosLosInformes();
        return ResponseEntity.ok(informes);
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<InformeModel> obtenerPorId(@PathVariable String id) {
        Optional<InformeModel> informeOpt = informeService.buscarPorId(id);
        return informeOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<InformeModel> crearInforme(@RequestBody InformeModel informe) {
        InformeModel nuevoInforme = informeService.guardarInforme(informe);
        return new ResponseEntity<>(nuevoInforme, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InformeModel> actualizarInforme(@PathVariable String id,
                                                          @RequestBody InformeModel informe) {
        if (!id.equals(informe.getId())) {
            return ResponseEntity.badRequest().build();
        }

        InformeModel actualizado = informeService.actualizarInforme(informe);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInforme(@PathVariable String id) {
        boolean eliminado = informeService.eliminarInforme(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
