package com.tpe.microservicioadministracion.service;

import com.tpe.microservicioadministracion.domain.entity.Administrador;
import com.tpe.microservicioadministracion.domain.entity.Tarifa;
import com.tpe.microservicioadministracion.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaService {
    @Autowired
    private final TarifaRepository tarifaRepository;

    public TarifaService(TarifaRepository tarifaRepository) {
        this.tarifaRepository = tarifaRepository;
    }

    @Transactional
    public List<Tarifa> getTarifas() {
        return tarifaRepository.findAll();
    }

    @Transactional
    public Tarifa getTarifaId(Long id) {
        Optional<Tarifa> tarifa = tarifaRepository.findById(id);
        if (tarifa.isPresent()){
            return tarifa.get();
        }else{
            throw new RuntimeException("No se encontró la tarifa con ID: " + id);
        }
    }

    @Transactional
    public Tarifa guardarTarifa(Tarifa tarifa) {
        try {
            return tarifaRepository.save(tarifa);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar la tarifa: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Tarifa modificicarTarifa(Long id, Tarifa tarifaNueva) {
        Optional<Tarifa> tarifaExistente = tarifaRepository.findById(id);
        if (tarifaExistente.isPresent()){
            Tarifa tarifa = tarifaExistente.get();
            tarifa.setFecha_inicio(tarifaNueva.getFecha_inicio());
            tarifa.setTarifaNormal(tarifaNueva.getTarifaNormal());
            tarifa.setTarifaExtra(tarifaNueva.getTarifaExtra());
            return tarifaRepository.save(tarifa);
        }else{
            throw new RuntimeException("No se encontró la tarifa con ID: " + id);
        }
    }

    @Transactional
    public boolean eliminarTarifa(Long id) {
        Optional<Tarifa> tarifa = tarifaRepository.findById(id);
        if (tarifa.isPresent()) {
            tarifaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
