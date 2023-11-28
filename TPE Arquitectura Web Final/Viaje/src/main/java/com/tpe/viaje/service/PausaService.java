package com.tpe.viaje.service;

import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.repository.PausaRepositoryMongodb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class PausaService {

    private final PausaRepositoryMongodb pausaRepository;
    @Autowired
    public PausaService(PausaRepositoryMongodb pausaRepository) {
        this.pausaRepository = pausaRepository;
    }

    @Transactional(readOnly = true)
    public List<Pausa> obtenerPausa() {
        return pausaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pausa obtenerPausaPorId(String id) {
        Optional<Pausa> pausa = pausaRepository.findById(id);
        if (pausa.isPresent()){
            return pausa.get();
        }else{
            throw new RuntimeException("No se encontró la pausa con id: " + id);
        }
    }

    @Transactional(readOnly = true)
    public List<Pausa> obtenerPausasMonopatinId(Long id) {
        List<Pausa> pausas = pausaRepository.findByMonopatinId(id);
        if (!pausas.isEmpty()){
            return pausas;
        }else{
            throw new RuntimeException("No se encontraron pausa del monopatin con id: " + id);
        }
    }

    @Transactional(readOnly = true)
    public List<List<Integer>> obtenerTiempDePausaPorMonopatinId() {
        List<List<Integer>> pausas = pausaRepository.findUsoByMonopatinId();
        if (!pausas.isEmpty()){
            return pausas;
        }else{
            throw new RuntimeException("No se encontraron los datos de uso del monopatin" );
        }
    }

    @Transactional
    public Pausa guardarPausa(Pausa pausa) {
        try {
            return pausaRepository.save(pausa);

        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar la pausa: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Pausa actualizarPausa(String id, Pausa pausaNueva) {
        Optional<Pausa> pausaObt = pausaRepository.findById(id);
        if (pausaObt.isPresent()){
            Pausa pausa = pausaObt.get();
            pausa.setMotivo(pausaNueva.getMotivo());
            pausa.setId_viaje(pausaNueva.getId_viaje());
            pausa.setTiempo(pausaNueva.getTiempo());
            return pausaRepository.save(pausa);
        }else{
            throw new RuntimeException("No se encontró la pausa con ID: " + id);
        }
    }

    @Transactional
    public boolean borrarPausa(String id) {
        Optional<Pausa> pausa = pausaRepository.findById(id);
        if (pausa.isPresent()) {
            pausaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
