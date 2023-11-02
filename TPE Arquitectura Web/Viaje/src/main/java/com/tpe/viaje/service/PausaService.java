package com.tpe.viaje.service;

import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.Entity.Viaje;
import com.tpe.viaje.repository.PausaRepository;
import com.tpe.viaje.repository.ViajeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PausaService {

    private final PausaRepository pausaRepository;
    private final ViajeService viajeService;
    @Autowired
    public PausaService(PausaRepository pausaRepository,ViajeService viajeService) {
        this.pausaRepository = pausaRepository;
        this.viajeService = viajeService;
    }

    public List<Pausa> obtenerPausa() {
        return pausaRepository.findAll();
    }

    public Pausa obtenerPausaPorId(Long id) {
        Optional<Pausa> pausa = pausaRepository.findById(id);
        if (pausa.isPresent()){
            return pausa.get();
        }else{
            throw new RuntimeException("No se encontró el viaje con id: " + id);
        }
    }
    public List<Pausa> obtenerPausaPorMonopatinId(Long id) {
        List<Pausa> pausas = pausaRepository.findByMonopatinId(id);
        if (!pausas.isEmpty()){
            return pausas;
        }else{
            throw new RuntimeException("No se encontró el viaje con id: " + id);
        }
    }
    public List<List<Integer>> obtenerTiempDePausaPorMonopatinId() {
        List<List<Integer>> pausas = pausaRepository.findUsoByMonopatinId();
        if (!pausas.isEmpty()){
            return pausas;
        }else{
            throw new RuntimeException("No se encontró el viaje con id: " );
        }
    }
    @Transactional
    public Pausa guardarPausa(Pausa pausa) {
        try {
            Pausa pausa1 = pausaRepository.save(pausa);
            viajeService.guardarPausa(pausa, pausa.getViaje().getId());
            return pausa1;

        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar la pausa: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Pausa actualizarPausa(Long id, Pausa pausaNueva) {
        Optional<Pausa> pausaObt = pausaRepository.findById(id);
        if (pausaObt.isPresent()){
            Pausa pausa = pausaObt.get();
            viajeService.eliminarPausa(pausa, pausa.getViaje().getId());
            pausa.setMotivo(pausaNueva.getMotivo());
            pausa.setViaje(pausaNueva.getViaje());
            pausa.setTiempo(pausaNueva.getTiempo());
            Pausa saveP= pausaRepository.save(pausa);
            viajeService.guardarPausa(pausa, pausa.getViaje().getId());
            return saveP;
        }else{
            throw new RuntimeException("No se encontró la carrera con ID: " + id);
        }
    }

    @Transactional
    public boolean borrarPausa(Long id) {
        Optional<Pausa> pausa = pausaRepository.findById(id);
        if (pausa.isPresent()) {
            viajeService.eliminarPausa(pausa.get(),pausa.get().getViaje().getId());
            pausaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
