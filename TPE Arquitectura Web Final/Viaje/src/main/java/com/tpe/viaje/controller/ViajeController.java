package com.tpe.viaje.controller;

import com.tpe.viaje.DTO.MonopatinPorUso;
import com.tpe.viaje.DTO.TotalFacturadoDTO;
import com.tpe.viaje.Entity.Viaje;
import com.tpe.viaje.security.AuthorityConstants;
import com.tpe.viaje.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vp/viajes")
public class ViajeController {
    private final ViajeService viajeService;
    @Autowired
    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    @GetMapping("/porkm")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerViajesPorKm() {
        try {
            List<Viaje> viajes = viajeService.obtenerViajeOrdenadosKm();
            if(viajes.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron viajes.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(viajes);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron viajes.");
        }
    }

    @GetMapping("/totalFacturado/mesInicio/{mesInicio}/mesFinal/{mesFinal}/anio/{anio}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerTotalFacturado(@PathVariable Integer mesInicio, @PathVariable Integer mesFinal, @PathVariable Integer anio) {
        try {
            TotalFacturadoDTO facturado = viajeService.calcularTotalFacturado(mesInicio, mesFinal, anio);
            return ResponseEntity.ok(facturado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo ejecutar");
        }
    }

    @GetMapping("/monopatinid")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerViajesPorMonopatin() {
        try {
            List<MonopatinPorUso> viajes = viajeService.obtenerViajesdeMonopatin();
            if (viajes != null && !viajes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(viajes);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron viajes.");
            }
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al acceder a la base de datos.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado.");
        }
    }

    @GetMapping("/cantviajesxanio/{cant}/{anio}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerMonopatinesCantViajesEnAnio(@PathVariable int cant, @PathVariable int anio) {
        try {
            List<Long> viajes = viajeService.obtenerViajesPorIDMonopatin(cant, anio);
            if(viajes.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron viajes.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(viajes);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron viajes.999");
        }
    }

    @GetMapping("/")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerViajes() {
        try {
            List<Viaje> viajes = viajeService.obtenerViajes();
            if(viajes.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron viajes.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(viajes);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron viajes.");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerViajePorid(@PathVariable String id) {
        try {
            Viaje viaje = viajeService.obtenerViajePorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(viaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el viaje con id: " + id);
        }
    }

    @PostMapping("/guardar")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> guardarViaje(@RequestBody Viaje viaje) {
        try {
            Viaje viajeGuardado = viajeService.guardarViaje(viaje);
            return ResponseEntity.status(HttpStatus.CREATED).body(viajeGuardado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar el viaje: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de viaje inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el viaje: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> actualizarViale(@PathVariable String id, @RequestBody Viaje viaje) {
        try {
            Viaje viajeActualizado = viajeService.actualizarViaje(id, viaje);
            return ResponseEntity.status(HttpStatus.OK).body(viajeActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos del viaje inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> borrarViaje(@PathVariable String id) {
        try {
            boolean viajeBorrado = viajeService.borrarViaje(id);
            if (viajeBorrado) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el viaje con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID del viaje inválido: " + e.getMessage());
        }
    }
}
