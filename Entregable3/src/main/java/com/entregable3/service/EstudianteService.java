package com.entregable3.service;

import com.entregable3.domain.Carrera;
import com.entregable3.domain.Estudiante;
import com.entregable3.repository.EstudianteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;

    @Autowired
    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public List<Estudiante> obtenerEstudiantesOrdenadosApellidos() {
        return estudianteRepository.findAllByOrderByApellidoAsc();
    }

    public List<Estudiante> obtenerEstudiantes() {
        return estudianteRepository.findAll();
    }

    public Estudiante obtenerEstudianteDni(int dni) {
        Optional<Estudiante> estudiante = estudianteRepository.findByDni(dni);
        if (estudiante.isPresent()){
            return estudiante.get();
        }else{
            throw new RuntimeException("No se encontró el estudiante con DNI: " + dni);
        }
    }

    @Transactional
    public Estudiante guardarEstudiante(Estudiante estudiante) {
        try {
            return estudianteRepository.save(estudiante);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar el estudiante: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Estudiante actualizarEstudiante(int id, Estudiante estudianteNuevo) {
        Optional<Estudiante> estudianteExistente = estudianteRepository.findById(id);
        if (estudianteExistente.isPresent()){
            Estudiante estudiante = estudianteExistente.get();
            estudiante.setNombre(estudianteNuevo.getNombre());
            estudiante.setApellido(estudianteNuevo.getApellido());
            estudiante.setEdad(estudianteNuevo.getEdad());
            estudiante.setGenero(estudianteNuevo.getGenero());
            estudiante.setCiudad(estudianteNuevo.getCiudad());
            estudiante.setNroLibreta(estudianteNuevo.getNroLibreta());
            return estudianteRepository.save(estudiante);
        }else{
            throw new RuntimeException("No se encontró la carrera con ID: " + id);
        }
    }

    @Transactional
    public boolean borrarEstudiante(int id) {
        Optional<Estudiante> estudiante = estudianteRepository.findById(id);
        if (estudiante.isPresent()) {
            estudianteRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Estudiante obtenerEstudianteLibreta(int numLibreta) {
        Optional<Estudiante> estudiante = estudianteRepository.findByNroLibreta(numLibreta);
        if (estudiante.isPresent()){
            return estudiante.get();
        }else{
            throw new RuntimeException("No se encontró el estudiante con numero de libreta: " + numLibreta);
        }
    }

    public List<Estudiante> obtenerEstudiantesPorGenero(String genero) {
        return estudianteRepository.findByGenero(genero);
    }
}
