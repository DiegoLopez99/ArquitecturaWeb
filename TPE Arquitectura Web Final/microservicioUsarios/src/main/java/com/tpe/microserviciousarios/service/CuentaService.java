package com.tpe.microserviciousarios.service;

import com.tpe.microserviciousarios.domain.Cuenta;
import com.tpe.microserviciousarios.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {
    private final CuentaRepository cuentaRepository;

    @Autowired
    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }
    @Transactional
    public List<Cuenta> getCuentas() {
        return cuentaRepository.findAll();
    }

    @Transactional
    public Cuenta getCuentaId(Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        if (cuenta.isPresent()){
            return cuenta.get();
        }else{
            throw new RuntimeException("No se encontró la cuenta con ID: " + id);
        }
    }

    @Transactional
    public Cuenta guardarCuenta(Cuenta cuenta) {
        try {
            return cuentaRepository.save(cuenta);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar la cuenta: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Cuenta modificicarCuenta(Long id, Cuenta cuentaNueva) {
        Optional<Cuenta> cuentaExistente = cuentaRepository.findById(id);
        if (cuentaExistente.isPresent()){
            Cuenta cuenta = cuentaExistente.get();
            cuenta.setFechaAlta(cuentaNueva.getFechaAlta());
            cuenta.setSaldo(cuentaNueva.getSaldo());
            return cuentaRepository.save(cuenta);
        }else{
            throw new RuntimeException("No se encontró la cuenta con ID: " + id);
        }
    }

    @Transactional
    public boolean eliminarCuenta(Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        if (cuenta.isPresent()) {
            cuentaRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
