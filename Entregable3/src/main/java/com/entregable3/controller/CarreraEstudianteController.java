package com.entregable3.controller;

import com.entregable3.domain.Carrera;
import com.entregable3.domain.Carrera_Estudiante;
import com.entregable3.domain.Estudiante;
import com.entregable3.dto.CarreraInscriptosDTO;
import com.entregable3.dto.EstudiantesPorCarreraPorCiudadDTO;
import com.entregable3.dto.MatriculaDTO;
import com.entregable3.dto.ReporteCarrerasDTO;
import com.entregable3.service.CarreraEstudianteService;
import com.entregable3.service.CarreraService;
import com.entregable3.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/carreraEstudiante")
public class CarreraEstudianteController {
    private final CarreraEstudianteService carreraEstudianteService;
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;

    @Autowired
    public CarreraEstudianteController(CarreraEstudianteService carreraEstudianteService, EstudianteService estudianteService,
                                       CarreraService carreraService) {
        this.carreraEstudianteService = carreraEstudianteService;
        this.estudianteService = estudianteService;
        this.carreraService = carreraService;
    }

    @PostMapping("/matricular")
    public ResponseEntity<Object> matricularEstudianteEnCarrera(@RequestBody MatriculaDTO request) {
        int estudianteId = request.getEstudianteId();
        Long carreraId = request.getCarreraId();
        String anioInscripcion = request.getAnioInscripcion();
        String anioGraduacion = request.getAnioGraduacion();
        int antiguedad = request.getAntiguedad();
        Estudiante estudiante =  estudianteService.obtenerEstudianteDni(estudianteId);
        Carrera carrera = carreraService.obtenerCarreraId(carreraId);

        if (estudiante != null && carrera != null) {
            Carrera_Estudiante matricula = carreraEstudianteService.matricularEstudianteEnCarrera(estudiante, carrera, anioInscripcion, anioGraduacion,antiguedad);
            return ResponseEntity.status(HttpStatus.CREATED).body(matricula);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante o carrera no encontrados.");
        }
    }

    @GetMapping("/carrera-ciudad/{ciudad}/{carrera}/")
    public ResponseEntity<Object> obtenerEstudiantesDeCarreraPorCiudad(
            @PathVariable String ciudad,
            @PathVariable String carrera
    ) {
        List<EstudiantesPorCarreraPorCiudadDTO> estudiantes = carreraEstudianteService.obtenerEstudiantesDeCarreraPorCiudad(ciudad, carrera);

        if (!estudiantes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(estudiantes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron estudiantes de la carrera "+ciudad+" que residan en la ciudad de "+carrera);
        }
    }

    @GetMapping("/inscriptos")
    public ResponseEntity<List<CarreraInscriptosDTO>> obtenerCarrerasConInscriptos() {
        List<CarreraInscriptosDTO> resultados = carreraEstudianteService.obtenerCarrerasConInscriptos();

        if (!resultados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultados);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/inscriptos-y-egresados")
    public ResponseEntity<List<ReporteCarrerasDTO>> getInscriptosYEgresadosPorAnio() {
        List<ReporteCarrerasDTO> resultados = carreraEstudianteService.getInscriptosYEgresadosPorAnio();

        if (!resultados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultados);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }
}
