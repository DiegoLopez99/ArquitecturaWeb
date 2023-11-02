package com.tpe.microservicioadministracion.service;

import com.tpe.microservicioadministracion.domain.clases.Cuenta;
import com.tpe.microservicioadministracion.domain.clases.Mantenimiento;
import com.tpe.microservicioadministracion.domain.clases.Monopatin;
import com.tpe.microservicioadministracion.domain.clases.Parada;
import com.tpe.microservicioadministracion.domain.entity.Administrador;
import com.tpe.microservicioadministracion.domain.entity.Tarifa;
import com.tpe.microservicioadministracion.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {
    @Autowired
    private final AdministradorRepository administradorRepository;
    @Autowired
    private final TarifaService tarifaService;
    @Autowired
    private final RestTemplate restTemplate;

    public AdministradorService(AdministradorRepository administradorRepository, TarifaService tarifaService, RestTemplate restTemplate) {
        this.administradorRepository = administradorRepository;
        this.tarifaService = tarifaService;
        this.restTemplate = restTemplate;
    }
    @Transactional
    public ResponseEntity<String> setMonopatinMantenimiento(Long idMonopatin){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Monopatin> response = restTemplate.exchange(
                "http://localhost:8003/monopatines/id/" + idMonopatin,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Monopatin>() {}

        );
        if (response.getStatusCode().is2xxSuccessful()){
            Monopatin monopatin = response.getBody();
            assert monopatin != null;
            if (monopatin.getEstado().equals("disponible")){
                this.agregarMantenimiento(idMonopatin);
                monopatin.setEstado("en mantenimiento");
                HttpEntity<Monopatin> requestEntity2 = new HttpEntity<>(monopatin, headers);
                ParameterizedTypeReference<Monopatin> responseType = new ParameterizedTypeReference<Monopatin>() {};
                ResponseEntity<Monopatin> response2 = restTemplate.exchange(
                        "http://localhost:8003/monopatines/" + idMonopatin,
                        HttpMethod.PUT,
                        requestEntity2,
                        responseType
                );
                if (response2.getStatusCode().is2xxSuccessful()) {
                    return new ResponseEntity<>("Monopatín cambiado a estado de mantenimiento con éxito", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Error al cambiar el estado del monopatín", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("El monopatín no está disponible para mantenimiento", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Monopatín no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> setMonopatinFinMantenimiento(Long idMonopatin, Long idMantenimiento) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Monopatin> response = restTemplate.exchange(
                "http://localhost:8003/monopatines/id/" + idMonopatin,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Monopatin>() {}

        );
        if (response.getStatusCode().is2xxSuccessful()){
            Monopatin monopatin = response.getBody();
            assert monopatin != null;
            if (monopatin.getEstado().equals("en mantenimiento")){
                this.finalizarMantenimiento(idMantenimiento);
                monopatin.setEstado("disponible");
                HttpEntity<Monopatin> requestEntity2 = new HttpEntity<>(monopatin, headers);
                ParameterizedTypeReference<Monopatin> responseType = new ParameterizedTypeReference<Monopatin>() {};
                ResponseEntity<Monopatin> response2 = restTemplate.exchange(
                        "http://localhost:8003/monopatines/" + idMonopatin,
                        HttpMethod.PUT,
                        requestEntity2,
                        responseType
                );
                if (response2.getStatusCode().is2xxSuccessful()) {
                    return new ResponseEntity<>("Monopatín cambiado a estado disponible con éxito", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Error al cambiar el estado del monopatín", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("El monopatín no está en mantenimiento", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Monopatín no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    private void agregarMantenimiento(Long idMonopatin) {
        HttpHeaders headers = new HttpHeaders();

        Mantenimiento mantenimientoNuevo = new Mantenimiento();
        mantenimientoNuevo.setIdMonopatin(idMonopatin);
        mantenimientoNuevo.setInicio(LocalDate.now());
        mantenimientoNuevo.setFinalizacion(null);
        mantenimientoNuevo.setReparado(false);
        HttpEntity<Mantenimiento> requestEntity = new HttpEntity<>(mantenimientoNuevo, headers);

        restTemplate.exchange(
                "http://localhost:8003/mantenimientos/guardar",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    private void finalizarMantenimiento(Long idMantenimiento) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Mantenimiento> response = restTemplate.exchange(
                "http://localhost:8003/mantenimientos/id/" + idMantenimiento,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Mantenimiento>() {}

        );
        if (response.getStatusCode().is2xxSuccessful()){
            Mantenimiento mantenimientoNuevo = response.getBody();
            assert mantenimientoNuevo != null;
            mantenimientoNuevo.setFinalizacion(LocalDate.now());
            mantenimientoNuevo.setReparado(true);
            HttpEntity<Mantenimiento> requestEntity2 = new HttpEntity<>(mantenimientoNuevo, headers);
            ParameterizedTypeReference<Mantenimiento> responseType = new ParameterizedTypeReference<Mantenimiento>() {};
            restTemplate.exchange(
                    "http://localhost:8003/mantenimientos/" + idMantenimiento,
                    HttpMethod.PUT,
                    requestEntity2,
                    responseType
            );
        }

    }

    @Transactional
    public List<Administrador> getAdministradores() {
        return administradorRepository.findAll();
    }

    @Transactional
    public Administrador getAdministradorId(Long id) {
        Optional<Administrador> admin = administradorRepository.findById(id);
        if (admin.isPresent()){
            return admin.get();
        }else{
            throw new RuntimeException("No se encontró el administrador con ID: " + id);
        }
    }

    @Transactional
    public Administrador guardarAdministrador(Administrador admin) {
        try {
            return administradorRepository.save(admin);
        } catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar el administrador: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Administrador modificicarAdministrador(Long id, Administrador adminNuevo) {
        Optional<Administrador> adminExistente = administradorRepository.findById(id);
        if (adminExistente.isPresent()){
            Administrador administrador = adminExistente.get();
            administrador.setNombreUsuario(adminNuevo.getNombreUsuario());
            administrador.setContrasenia(adminNuevo.getContrasenia());
            administrador.setRol(adminNuevo.getRol());
            return administradorRepository.save(administrador);
        }else{
            throw new RuntimeException("No se encontró el administrador con ID: " + id);
        }
    }

    @Transactional
    public boolean eliminarAdministrador(Long id) {
        Optional<Administrador> admin = administradorRepository.findById(id);
        if (admin.isPresent()) {
            administradorRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public ResponseEntity<Object> agregarMonopatin(Monopatin monopatin) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Monopatin> requestEntity = new HttpEntity<>(monopatin, headers);
        ParameterizedTypeReference<Monopatin> responseType = new ParameterizedTypeReference<Monopatin>() {};
        ResponseEntity<Monopatin> response = restTemplate.exchange(
                "http://localhost:8003/monopatines/guardar",
                HttpMethod.POST,
                requestEntity,
                responseType
        );
        if (response.getStatusCode() == HttpStatus.CREATED){
            return new ResponseEntity<>("Monopatín agregado con éxito", HttpStatus.CREATED);
        } else if (response.getStatusCode() == HttpStatus.CONFLICT){
            return new ResponseEntity<>("Error de integridad de datos al guardar el monopatin ", HttpStatus.CONFLICT);
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>("Datos del monopatin invalidos", HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>("Error al guardar el monopatin", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<Object> eliminarMonopatin(Long idMonopatin) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<Monopatin> responseType = new ParameterizedTypeReference<Monopatin>() {};
        ResponseEntity<Monopatin> response = restTemplate.exchange(
                "http://localhost:8003/monopatines/" + idMonopatin,
                HttpMethod.DELETE,
                requestEntity,
                responseType
        );
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>("Monopatín eliminado con éxito", HttpStatus.NO_CONTENT);
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Monopatín no encontrado", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("ID de monopatin inválido", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Object> agregarParada(Parada parada) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Parada> requestEntity = new HttpEntity<>(parada, headers);
        ParameterizedTypeReference<Parada> responseType = new ParameterizedTypeReference<Parada>() {};
        ResponseEntity<Parada> response = restTemplate.exchange(
                "http://localhost:8004/paradas/agregar",
                HttpMethod.POST,
                requestEntity,
                responseType
        );
        if (response.getStatusCode() == HttpStatus.CREATED){
            return new ResponseEntity<>("Parada agregada con éxito", HttpStatus.CREATED);
        } else if (response.getStatusCode() == HttpStatus.CONFLICT){
            return new ResponseEntity<>("Error de integridad de datos al guardar la parada", HttpStatus.CONFLICT);
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST){
            return new ResponseEntity<>("Datos de parada invalidos", HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>("Error al guardar la parada", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<Object> eliminarParada(Long idParada) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<Parada> responseType = new ParameterizedTypeReference<Parada>() {};
        ResponseEntity<Parada> response = restTemplate.exchange(
                "http://localhost:8004/paradas/" + idParada,
                HttpMethod.DELETE,
                requestEntity,
                responseType
        );
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return new ResponseEntity<>("Parada eliminada con éxito", HttpStatus.NO_CONTENT);
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return new ResponseEntity<>("Parada no encontrada", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("ID de parada inválido", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ResponseEntity<Object> agregarTarifa(Tarifa tarifa) {
        Tarifa nuevaTarifa = tarifaService.guardarTarifa(tarifa);

        if (nuevaTarifa != null) {
            return new ResponseEntity<>("Tarifa agregada con éxito", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Error al agregar la tarifa", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<Object> definirTarifaExtra(Long idTarifa, Float tarifaExtra) {
        Tarifa tarifa = tarifaService.getTarifaId(idTarifa);
        if (tarifa != null){
            tarifa.setTarifaExtra(tarifaExtra);
            tarifaService.modificicarTarifa(idTarifa, tarifa);
            return new ResponseEntity<>("Tarifa extra definida con éxito", HttpStatus.OK);
        } else{
            return new ResponseEntity<>("La tarifa no fue encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<String> anularCuenta(Long idCuenta) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Cuenta> response = restTemplate.exchange(
                "http://localhost:8001/cuentas/id/" + idCuenta,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Cuenta>() {}

        );
        if (response.getStatusCode().is2xxSuccessful()){
            Cuenta cuenta = response.getBody();
            assert cuenta != null;
            if (cuenta.getEstado().equals("activa")){
                cuenta.setEstado("suspendida");
                HttpEntity<Cuenta> requestEntity2 = new HttpEntity<>(cuenta, headers);
                ParameterizedTypeReference<Cuenta> responseType = new ParameterizedTypeReference<Cuenta>() {};
                ResponseEntity<Cuenta> response2 = restTemplate.exchange(
                        "http://localhost:8001/cuentas/" + idCuenta,
                        HttpMethod.PUT,
                        requestEntity2,
                        responseType
                );
                if (response2.getStatusCode().is2xxSuccessful()) {
                    return new ResponseEntity<>("Cuenta anulada con éxito", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Error al anular la cuenta", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("La cuenta ya esta anulada", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<Object> generarReporteMonopatinesKm(Float kmMinimo, Float kmMaximo) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "http://localhost:8003/monopatines/reporte/kmMinimo/"+ kmMinimo+ "/kmMaximo/"+kmMaximo,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
    }

    @Transactional
    public ResponseEntity<Object> generarReporteMonopatinesTiempoUsoConPausa() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "http://localhost:8003/monopatines/reporte/porTiempoConPausa",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
    }

    @Transactional
    public ResponseEntity<Object> generarReporteMonopatinesTiempoUso() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "http://localhost:8003/monopatines/reporte/porTiempo",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
    }

    @Transactional
    public ResponseEntity<Object> obtenerMonopatinesConMasViajesEnAnio(Integer cant, Integer anio) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "http://localhost:8003/monopatines/cantViajes"+cant+"/anio/"+anio,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
    }

    @Transactional
    public ResponseEntity<Object> obtenerTotalFacturado(String mesInicio, String mesFinal, Integer anio) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "http://localhost:8080/viajes/totalFacturado/mesInicio/"+mesInicio+"mesFinal/"+mesFinal+"anio/"+anio,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
    }

    @Transactional
    public ResponseEntity<Object> obtenerMonopatinesOperacionYmantenimiento() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "http://localhost:8003/monopatines/enOperacionYMantenimiento",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
    }

    @Transactional
    public ResponseEntity<Object> getMonopatinesPorUbicacion() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                "http://localhost:8003/monopatines/monopatinesPorUbicacion",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );
    }
}
