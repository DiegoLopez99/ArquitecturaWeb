package com.tpe.microservicioadministracion.service;

import com.tpe.microservicioadministracion.domain.entity.Rol;
import com.tpe.microservicioadministracion.domain.entity.Tarifa;
import com.tpe.microservicioadministracion.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {
    @Autowired
    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional
    public List<Rol> getRoles() {
        return rolRepository.findAll();
    }

    @Transactional
    public Rol getRolId(Long id) {
        Optional<Rol> rol = rolRepository.findById(id);
        if (rol.isPresent()){
            return rol.get();
        }else{
            throw new RuntimeException("No se encontró el rol con ID: " + id);
        }
    }

    @Transactional
    public Rol guardarRol(Rol rol) {
        try {
            return rolRepository.save(rol);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar el rol: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Rol modificicarRol(Long id, Rol rolNuevo) {
        Optional<Rol> rolExistente = rolRepository.findById(id);
        if (rolExistente.isPresent()){
            Rol rol = rolExistente.get();
            return rolRepository.save(rol);
        }else{
            throw new RuntimeException("No se encontró el rol con ID: " + id);
        }
    }

    @Transactional
    public boolean eliminarRol(Long id) {
        Optional<Rol> rol = rolRepository.findById(id);
        if (rol.isPresent()) {
            rolRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
