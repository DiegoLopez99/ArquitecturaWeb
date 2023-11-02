package com.tpe.microservicioparada.controller;

import com.tpe.microservicioparada.domain.Parada;
import com.tpe.microservicioparada.service.ParadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/paradas")
public class ParadaController {
    @Autowired
    private final ParadaService paradaService;

    public ParadaController(ParadaService paradaService) {
        this.paradaService = paradaService;
    }

    @GetMapping("/")
    public ResponseEntity<Object> obtenerParadas() {
        try {
            List<Parada> paradas = paradaService.getParadas();
            if(paradas.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron paradas.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(paradas);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron paradas.");
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> obtenerParadaPorId(@PathVariable Long id) {
        try {
            Parada parada = paradaService.getParadaId(id);
            return ResponseEntity.status(HttpStatus.OK).body(parada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la parada con ID: " + id);
        }
    }

    @PostMapping("/agregar")
    public ResponseEntity<Object> agregarParada(@RequestBody Parada parada) {
        try {
            Parada paradaGuardada = paradaService.guardarParada(parada);
            return ResponseEntity.status(HttpStatus.CREATED).body(paradaGuardada);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar la parada: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de parada inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la parada: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> modificarParada(@PathVariable Long id, @RequestBody Parada paradaNueva) {
        try {
            Parada paradaActualizada = paradaService.modificicarParada(id, paradaNueva);
            return ResponseEntity.status(HttpStatus.OK).body(paradaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de parada inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarParada(@PathVariable Long id) {
        try {
            boolean paradaBorrada = paradaService.eliminarParada(id);
            if (paradaBorrada) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la parada con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de parada inválido: " + e.getMessage());
        }
    }
}
