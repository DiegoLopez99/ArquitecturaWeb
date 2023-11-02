package com.tpe.viaje.service;

import com.tpe.viaje.DTO.TotalFacturadoDTO;
import com.tpe.viaje.Entity.Pausa;
import com.tpe.viaje.Entity.Viaje;
import com.tpe.viaje.repository.ViajeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {
    private final ViajeRepository viajeRepository;
    @Autowired
    public ViajeService(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
    }

    public List<Viaje> obtenerViajeOrdenadosKm() {
        return viajeRepository.findAllByOrderByKmAsc();
    }
    public List<Viaje> obtenerViajes() {
        return viajeRepository.findAll();
    }

    public List<List<Integer>> obtenerViajesdeMonopatin() {
        return viajeRepository.findByid_Monopatin();
    }

    public Viaje obtenerViajePorId(Long id) {
        Optional<Viaje> viaje = viajeRepository.findById(id);
        if (viaje.isPresent()){
            return viaje.get();
        }else{
            throw new RuntimeException("No se encontró el viaje con id: " + id);
        }
    }
   public List<Long> obtenerViajesPorIDMonopatin(int x, int anio){
        List<Long> viajes = viajeRepository.findByid_monopatin(x,anio);
        if (viajes.isEmpty()){
            throw new RuntimeException("No se encontraron viajes : ");
        }
        else {
            return viajes;
        }
    }
    @Transactional
    public void guardarPausa(Pausa p, long v_id){
        Optional<Viaje> viajeObt = viajeRepository.findById(v_id);
        if (viajeObt.isPresent()){
            Viaje viaje = viajeObt.get();
            viaje.addPausa(p);
            viajeRepository.save(viaje);
        }else{
            throw new RuntimeException("No se encontró el viaje con ID: " + v_id);
        }
    }
    @Transactional
    public void eliminarPausa(Pausa p, long v_id){
        Optional<Viaje> viajeObt = viajeRepository.findById(v_id);
        if (viajeObt.isPresent()){
            Viaje viaje = viajeObt.get();
            viaje.removePausa(p);
            viajeRepository.save(viaje);
        }else{
            throw new RuntimeException("No se encontró el viaje con ID: " + v_id);
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
    public Viaje actualizarViaje(Long id, Viaje viajeNuevo) {
        Optional<Viaje> viajeObt = viajeRepository.findById(id);
        if (viajeObt.isPresent()){
            Viaje viaje = viajeObt.get();
            viaje.setCosto(viajeNuevo.getCosto());
            viaje.setFin(viajeNuevo.getFin());
            viaje.setInicio(viajeNuevo.getInicio());
            viaje.setKm(viajeNuevo.getKm());
            return viajeRepository.save(viaje);
        }else{
            throw new RuntimeException("No se encontró el viaje con ID: " + id);
        }
    }

    @Transactional
    public boolean borrarViaje(Long id) {
        Optional<Viaje> viaje = viajeRepository.findById(id);
        if (viaje.isPresent()) {
            viajeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public TotalFacturadoDTO calcularTotalFacturado(Integer mesInicio, Integer mesFinal, Integer anio) {
        float totalFacturado = viajeRepository.calcularTotalFacturadoEnRango(mesInicio, mesFinal, anio);
        TotalFacturadoDTO facturado = new TotalFacturadoDTO();
        facturado.setMesInicio(mesInicio);
        facturado.setMesFinal(mesFinal);
        facturado.setAnio(anio);
        facturado.setTotalFacturado(totalFacturado);
        return facturado;
    }
}
