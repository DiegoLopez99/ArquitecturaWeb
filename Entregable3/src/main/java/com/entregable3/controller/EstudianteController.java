package com.entregable3.controller;

import com.entregable3.domain.Estudiante;
import com.entregable3.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {
    private final EstudianteService estudianteService;

    @Autowired
    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/ordenados")
    public ResponseEntity<Object> obtenerEstudiantesOrdenadosApellido() {
        try {
            List<Estudiante> estudiantes = estudianteService.obtenerEstudiantesOrdenadosApellidos();
            if(estudiantes.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron estudiantes.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(estudiantes);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron estudiantes.");
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> obtenerEstudiantes() {
        try {
            List<Estudiante> estudiantes = estudianteService.obtenerEstudiantes();
            if(estudiantes.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron estudiantes.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(estudiantes);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron estudiantes.");
        }
    }

    @GetMapping("/obtener/dni/{dni}")
    public ResponseEntity<Object> obtenerEstudiantePorDni(@PathVariable int dni) {
        try {
            Estudiante estudiante = estudianteService.obtenerEstudianteDni(dni);
            return ResponseEntity.status(HttpStatus.OK).body(estudiante);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el estudiante con DNI: " + dni);
        }
    }

    @GetMapping("/numLibreta/{numLibreta}")
    public ResponseEntity<Object> obtenerEstudiantePorNumLibreta(@PathVariable int numLibreta) {
        try {
            Estudiante estudiante = estudianteService.obtenerEstudianteLibreta(numLibreta);
            return ResponseEntity.status(HttpStatus.OK).body(estudiante);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el estudiante con numero de libreta: " + numLibreta);
        }
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<Object> obtenerEstudiantesPorGenero(@PathVariable String genero) {
        try {
            List<Estudiante> estudiantes = estudianteService.obtenerEstudiantesPorGenero(genero);
            if(estudiantes.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron estudiantes.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(estudiantes);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron estudiantes.");
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Object> guardarEstudiante(@RequestBody Estudiante estudiante) {
        try {
            Estudiante estudianteGuardado = estudianteService.guardarEstudiante(estudiante);
            return ResponseEntity.status(HttpStatus.CREATED).body(estudianteGuardado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar el estudiante: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de estudiante inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el estudiante: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarEstudiante(@PathVariable int id, @RequestBody Estudiante estudianteNuevo) {
        try {
            Estudiante estudianteActualizado = estudianteService.actualizarEstudiante(id, estudianteNuevo);
            return ResponseEntity.status(HttpStatus.OK).body(estudianteActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de estudiante inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrarEstudiante(@PathVariable int id) {
        try {
            boolean estudianteBorrado = estudianteService.borrarEstudiante(id);
            if (estudianteBorrado) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el estudiante con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de estudiante inválido: " + e.getMessage());
        }
    }
}
