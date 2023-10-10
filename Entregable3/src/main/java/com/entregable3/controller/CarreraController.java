package com.entregable3.controller;

import com.entregable3.domain.Carrera;
import com.entregable3.service.CarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carreras")
public class CarreraController {
    private final CarreraService carreraService;

    @Autowired
    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping("/ordenadas")
    public ResponseEntity<Object> obtenerCarrerasOrdenadas() {
        try {
            List<Carrera> carreras = carreraService.obtenerCarrerasOrdenadas();
            if(carreras.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron carreras.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(carreras);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron carreras.");
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> obtenerCarreras() {
        try {
            List<Carrera> carreras = carreraService.obtenerCarreras();
            if(carreras.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron carreras.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(carreras);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron carreras.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerCarreraPorId(@PathVariable Long id) {
        try {
            Carrera carrera = carreraService.obtenerCarreraId(id);
            return ResponseEntity.status(HttpStatus.OK).body(carrera);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la carrera con ID: " + id);
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Object> guardarCarrera(@RequestBody Carrera carrera) {
        try {
            Carrera carreraGuardada = carreraService.guardarCarrera(carrera);
            return ResponseEntity.status(HttpStatus.CREATED).body(carreraGuardada);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar la carrera: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de carrera inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la carrera: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarCarrera(@PathVariable Long id, @RequestBody Carrera carreraNueva) {
        try {
            Carrera carreraActualizada = carreraService.actualizarCarrera(id, carreraNueva);
            return ResponseEntity.status(HttpStatus.OK).body(carreraActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de carrera inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrarCarrera(@PathVariable Long id) {
        try {
            boolean carreraBorrada = carreraService.borrarCarrera(id);
            if (carreraBorrada) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la carrera con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de carrera inválido: " + e.getMessage());
        }
    }

}
