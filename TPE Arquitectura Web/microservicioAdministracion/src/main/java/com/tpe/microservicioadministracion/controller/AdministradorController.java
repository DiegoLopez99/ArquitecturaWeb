package com.tpe.microservicioadministracion.controller;

import com.tpe.microservicioadministracion.domain.clases.Monopatin;
import com.tpe.microservicioadministracion.domain.clases.Parada;
import com.tpe.microservicioadministracion.domain.entity.Administrador;
import com.tpe.microservicioadministracion.domain.entity.Tarifa;
import com.tpe.microservicioadministracion.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {
    @Autowired
    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @PutMapping("/monopatines/enMantenimiento{idMonopatin}")
    public ResponseEntity<String> setMonopatinMantenimiento(@PathVariable Long idMonopatin) {
        return administradorService.setMonopatinMantenimiento(idMonopatin);
    }

    @PutMapping("/monopatines/finMantenimiento/monopatin/{idMonopatin}/mantenimiento/{idMantenimiento}")
    public ResponseEntity<String> setMonopatinFinMantenimiento(@PathVariable Long idMonopatin, @PathVariable Long idMantenimiento) {
        return administradorService.setMonopatinFinMantenimiento(idMonopatin, idMantenimiento);
    }

    @PostMapping("/monopatines/agregar")
    public ResponseEntity<Object> agregarMonopatin(@RequestBody Monopatin monopatin){
        return administradorService.agregarMonopatin(monopatin);
    }

    @DeleteMapping ("/monopatines/{idMonopatin}")
    public ResponseEntity<Object> eliminarMonopatin(@PathVariable Long idMonopatin){
        return administradorService.eliminarMonopatin(idMonopatin);
    }

    @PostMapping("/paradas/agregar")
    public ResponseEntity<Object> agregarParada(@RequestBody Parada parada){
        return administradorService.agregarParada(parada);
    }

    @DeleteMapping ("/paradas/{idParada}")
    public ResponseEntity<Object> eliminarParada(@PathVariable Long idParada){
        return administradorService.eliminarParada(idParada);
    }

    @PostMapping ("/tarifas/agregar")
    public ResponseEntity<Object> agregarTarifa(@RequestBody Tarifa tarifa){
        return administradorService.agregarTarifa(tarifa);
    }

    @PutMapping ("/tarifas/id/{idTarifa}/extra/{tarifaExtra}")
    public ResponseEntity<Object> definirTarifaExtra(@PathVariable Long idTarifa, @PathVariable Float tarifaExtra){
        return administradorService.definirTarifaExtra(idTarifa, tarifaExtra);
    }

    @PutMapping("/cuentas/anularCuenta/{idCuenta}")
    public ResponseEntity<String> anularCuenta(@PathVariable Long idCuenta) {
        return administradorService.anularCuenta(idCuenta);
    }

    @GetMapping("/monopatines/reporte/kmMinimo/{kmMinimo}/kmMaximo/{kmMaximo}")
    public ResponseEntity<Object> getReporteMonopatinesKm(@PathVariable Float kmMinimo, @PathVariable Float kmMaximo){
        return administradorService.generarReporteMonopatinesKm(kmMinimo, kmMaximo);
    }

    @GetMapping("/monopatines/monopatinesPorUbicacion")
    public ResponseEntity<Object> getMonopatinesPorUbicacion(){
        return administradorService.getMonopatinesPorUbicacion();
    }

    @GetMapping("/monopatines/reporte/porTiempoConPausa")
    public ResponseEntity<Object> getReporteMonopatinesPorTiempoConPausa(){
        return administradorService.generarReporteMonopatinesTiempoUsoConPausa();
    }

    @GetMapping("/monopatines/reporte/porTiempo")
    public ResponseEntity<Object> getReporteMonopatinesPorTiempo(){
        return administradorService.generarReporteMonopatinesTiempoUso();
    }

    @GetMapping("/monopatines/cantViajes/{cantViajes}/anio/{anio}")
    public ResponseEntity<Object> getMonopatinesConMasViajesEnAnio(@PathVariable Integer cantViajes, @PathVariable Integer anio){
        return administradorService.obtenerMonopatinesConMasViajesEnAnio(cantViajes, anio);
    }

    @GetMapping("/viajes/totalFacturado/mesInicio/{mesInicio}/mesFinal/{mesFinal}/anio/{anio}")
    public ResponseEntity<Object> getTotalFacturado(@PathVariable String mesInicio, @PathVariable String mesFinal, @PathVariable Integer anio){
        return administradorService.obtenerTotalFacturado(mesInicio, mesFinal, anio);
    }

    @GetMapping("/monopatines/enOperacionYMantenimiento")
    public ResponseEntity<Object> getMonopatinesOperacionYmantenimiento(){
        return administradorService.obtenerMonopatinesOperacionYmantenimiento();
    }

    @GetMapping("/")
    public ResponseEntity<Object> obtenerAdministradores() {
        try {
            List<Administrador> administradores = administradorService.getAdministradores();
            if(administradores.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron administradores.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(administradores);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron administradores.");
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> obtenerAdministradorPorId(@PathVariable Long id) {
        try {
            Administrador administrador = administradorService.getAdministradorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(administrador);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el administrador con ID: " + id);
        }
    }

    @PostMapping("/agregar")
    public ResponseEntity<Object> agregarAdministrador(@RequestBody Administrador admin) {
        try {
            Administrador adminGuardado = administradorService.guardarAdministrador(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body(adminGuardado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar el administrador: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos del administrador inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el administrador: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> modificarAdministrador(@PathVariable Long id, @RequestBody Administrador adminNuevo) {
        try {
            Administrador adminActualizado = administradorService.modificicarAdministrador(id, adminNuevo);
            return ResponseEntity.status(HttpStatus.OK).body(adminActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de administrador inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarAdministrador(@PathVariable Long id) {
        try {
            boolean adminBorrado = administradorService.eliminarAdministrador(id);
            if (adminBorrado) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el administrador con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID del administrador inválido: " + e.getMessage());
        }
    }

}
