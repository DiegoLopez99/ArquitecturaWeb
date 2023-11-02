package com.tpe.viaje.controller;

import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.Entity.Viaje;
import com.tpe.viaje.service.PausaService;
import com.tpe.viaje.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/pausas")
public class PausaController {

    private final PausaService pausaService;
    @Autowired
    public PausaController(PausaService pausaService) {
        this.pausaService = pausaService;
    }


    @GetMapping("/")
    public ResponseEntity<Object> obtenerPausas() {
        try {
            List<Pausa> pausas = pausaService.obtenerPausa();
            if(pausas.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron pausas.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(pausas);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron pausas.");
        }
    }

    @GetMapping("/obtener/id/{id}")
    public ResponseEntity<Object> obtenerPausaPorid(@PathVariable Long id) {
        try {
            Pausa pausa = pausaService.obtenerPausaPorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(pausa);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el viaje con id: " + id);
        }
    }
    @GetMapping("/obtenerPorMonopatin/MonopatinId/{id}")
    public ResponseEntity<Object> obtenerPausaPorMonotainid(@PathVariable Long id) {
        try {
            List<Pausa> pausa = pausaService.obtenerPausaPorMonopatinId(id);
            return ResponseEntity.status(HttpStatus.OK).body(pausa);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el viaje con id: " + id);
        }
    }
    @GetMapping("/obtenerTiempoMonopatin")
    public ResponseEntity<Object> obtenerTiempoPausaPorMonotainid() {
        try {
            List<List<Integer>> pausa = pausaService.obtenerTiempDePausaPorMonopatinId();
            return ResponseEntity.status(HttpStatus.OK).body(pausa);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el viaje con id: " );
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Object> guardarPausa(@RequestBody Pausa pausa) {
        try {
            Pausa pausaGuardada = pausaService.guardarPausa(pausa);
            return ResponseEntity.status(HttpStatus.CREATED).body(pausaGuardada);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar el viaje: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de viaje inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el viaje: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarPausa(@PathVariable Long id, @RequestBody Pausa pausa) {
        try {
            Pausa pausaActualizado = pausaService.actualizarPausa(id, pausa);
            return ResponseEntity.status(HttpStatus.OK).body(pausaActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos del viaje inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrarPausa(@PathVariable Long id) {
        try {
            boolean pausaBorrada = pausaService.borrarPausa(id);
            if (pausaBorrada) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la pausa con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de la pausa inválido: " + e.getMessage());
        }
    }
}
