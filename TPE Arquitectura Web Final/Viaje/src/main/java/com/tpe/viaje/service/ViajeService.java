package com.tpe.viaje.service;

import com.tpe.viaje.DTO.MonopatinPorUso;
import com.tpe.viaje.DTO.TotalFacturadoDTO;
import com.tpe.viaje.Entity.Viaje;
import com.tpe.viaje.repository.ViajeRepositoryMongodb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {
    private final ViajeRepositoryMongodb viajeRepository;
    @Autowired
    public ViajeService(ViajeRepositoryMongodb viajeRepository) {
        this.viajeRepository = viajeRepository;
    }

    @Transactional(readOnly = true)
    public List<Viaje> obtenerViajeOrdenadosKm() {
        Sort sortByKmAsc = Sort.by("km").descending();
        return viajeRepository.findAll(sortByKmAsc);
    }

    @Transactional(readOnly = true)
    public List<Viaje> obtenerViajes() {
        return viajeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MonopatinPorUso> obtenerViajesdeMonopatin() {
        return viajeRepository.obtenerUsoMonopatin();
    }

    @Transactional(readOnly = true)
    public Viaje obtenerViajePorId(String id) {
        Optional<Viaje> viaje = viajeRepository.findById(id);
        if (viaje.isPresent()){
            return viaje.get();
        }else{
            throw new RuntimeException("No se encontró el viaje con id: " + id);
        }
    }
    @Transactional(readOnly = true)
   public List<Long> obtenerViajesPorIDMonopatin(int x, int anio){
        List<Long> idMonopatines = viajeRepository.obtenerIdMonopatinesMasViajesAnio(x, anio);
        // Agrega este registro de depuración
        System.out.println("ID de Monopatines obtenidos: " + idMonopatines);
        if (idMonopatines.isEmpty()){
            throw new RuntimeException("No se encontraron monopatines con mas de "+x+" viajes en el año "+anio);
        }
        else {
            return idMonopatines;
        }
    }

    @Transactional
    public Viaje guardarViaje(Viaje viaje) {
        try {
            return viajeRepository.save(viaje);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de los datos al guardar el viaje: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Viaje actualizarViaje(String id, Viaje viajeNuevo) {
        Optional<Viaje> viajeObt = viajeRepository.findById(id);
        if (viajeObt.isPresent()){
            Viaje viaje = viajeObt.get();
            viaje.setCosto(viajeNuevo.getCosto());
            viaje.setFin(viajeNuevo.getFin());
            viaje.setInicio(viajeNuevo.getInicio());
            viaje.setKm(viajeNuevo.getKm());
            viaje.setId_monopatin(viajeNuevo.getId_monopatin());
            return viajeRepository.save(viaje);
        }else{
            throw new RuntimeException("No se encontró el viaje con ID: " + id);
        }
    }

    @Transactional
    public boolean borrarViaje(String id) {
        Optional<Viaje> viaje = viajeRepository.findById(id);
        if (viaje.isPresent()) {
            viajeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public TotalFacturadoDTO calcularTotalFacturado(Integer mesInicio, Integer mesFinal, Integer anio) {
        return viajeRepository.calcularTotalFacturadoEnRango(mesInicio, mesFinal, anio);
    }
}
