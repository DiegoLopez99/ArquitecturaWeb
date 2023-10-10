package com.entregable3.service;

import com.entregable3.domain.Carrera;
import com.entregable3.repository.CarreraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CarreraService {
    private final CarreraRepository carreraRepository;

    @Autowired
    public CarreraService(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    public List<Carrera> obtenerCarrerasOrdenadas() {
        return carreraRepository.findAllByOrderByNombreAsc();
    }

    public List<Carrera> obtenerCarreras() {
        return carreraRepository.findAll();
    }

    public Carrera obtenerCarreraId(Long id) {
        Optional<Carrera> carrera = carreraRepository.findById(id);
        if (carrera.isPresent()){
            return carrera.get();
        }else{
            throw new RuntimeException("No se encontró la carrera con ID: " + id);
        }
    }

    @Transactional
    public Carrera guardarCarrera(Carrera carrera){
        try {
            return carreraRepository.save(carrera);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar la carrera: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Carrera actualizarCarrera(Long id, Carrera carreraNueva) {
        Optional<Carrera> carreraExistente = carreraRepository.findById(id);
        if (carreraExistente.isPresent()){
            Carrera carrera = carreraExistente.get();
            carrera.setNombre(carreraNueva.getNombre());
            carrera.setDuracion(carreraNueva.getDuracion());
            return carreraRepository.save(carrera);
        }else{
            throw new RuntimeException("No se encontró la carrera con ID: " + id);
        }
    }

    @Transactional
    public boolean borrarCarrera(Long id){
        Optional<Carrera> carrera = carreraRepository.findById(id);
        if (carrera.isPresent()) {
            carreraRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
