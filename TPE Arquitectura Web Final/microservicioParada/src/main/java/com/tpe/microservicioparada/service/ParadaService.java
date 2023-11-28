package com.tpe.microservicioparada.service;

import com.tpe.microservicioparada.domain.Parada;
import com.tpe.microservicioparada.repository.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParadaService {
    @Autowired
    private final ParadaRepository paradaRepository;

    public ParadaService(ParadaRepository paradaRepository) {
        this.paradaRepository = paradaRepository;
    }

    @Transactional
    public List<Parada> getParadas() {
        return paradaRepository.findAll();
    }

    @Transactional
    public Parada getParadaId(Long id) {
        Optional<Parada> parada = paradaRepository.findById(id);
        if (parada.isPresent()){
            return parada.get();
        }else{
            throw new RuntimeException("No se encontró la parada con ID: " + id);
        }
    }

    @Transactional
    public Parada guardarParada(Parada parada) {
        try {
            return paradaRepository.save(parada);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar la parada: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Parada modificicarParada(Long id, Parada paradaNueva) {
        Optional<Parada> paradaExistente = paradaRepository.findById(id);
        if (paradaExistente.isPresent()){
            Parada parada = paradaExistente.get();
            parada.setLatitud(paradaNueva.getLatitud());
            parada.setLongitud(paradaNueva.getLongitud());
            parada.setCapacidad(paradaNueva.getCapacidad());
            return paradaRepository.save(parada);
        }else{
            throw new RuntimeException("No se encontró la parada con ID: " + id);
        }
    }

    @Transactional
    public boolean eliminarParada(Long id) {
        Optional<Parada> parada = paradaRepository.findById(id);
        if (parada.isPresent()) {
            paradaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
