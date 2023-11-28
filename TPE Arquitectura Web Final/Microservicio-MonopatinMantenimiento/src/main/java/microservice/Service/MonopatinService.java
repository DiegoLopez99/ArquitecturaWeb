package microservice.Service;

import microservice.Model.Dto.MonopatinPorUso;
import microservice.Model.Dto.Viaje;
import microservice.Model.Mantenimiento;
import microservice.Model.Monopatin;
import microservice.Repository.MonopatinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MonopatinService {
    @Autowired
    private MonopatinRepository monoRepo;

    @Autowired
    private RestTemplate restTemplate;
    public MonopatinService(MonopatinRepository m, RestTemplate restTemplate){
        monoRepo = m;
        this.restTemplate = restTemplate;
    }

    public List<Monopatin> obtenerMonopatines(){
        return monoRepo.findAll();
    }

    public Monopatin obtenerMonopatinPorId(@PathVariable Long id){
        Optional<Monopatin> monopatin = monoRepo.findById(id);
        if (monopatin.isPresent()){
            return monopatin.get();
        }else{
            throw new RuntimeException("No se encontró el monopatin con ID: " + id);
        }
    }
    @Transactional
    public Monopatin guardarMonopatin(Monopatin monopatin){
        try {
            return monoRepo.save(monopatin);
        }catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar el monopatin: " + e.getMessage(), e);
        }
    }
    @Transactional
    public Monopatin actualizarMonopatin(Long id, Monopatin monopatin){
        Optional<Monopatin> monopatinExistente = monoRepo.findById(id);
        if (monopatinExistente.isPresent()){
            Monopatin monopatin1 = monopatinExistente.get();
            monopatin1.setEstado(monopatin.getEstado());
            monopatin1.setUbicacion(monopatin.getUbicacion());
            monopatin1.setKm_recorridos(monopatin.getKm_recorridos());
            return monoRepo.save(monopatin1);
        }else{
            throw new RuntimeException("No se encontró el monopatin con ID: " + id);
        }
    }

    @Transactional
    public boolean borrarMonopatin(Long id){
        Optional<Monopatin> monopatin = monoRepo.findById(id);
        if (monopatin.isPresent()) {
            monoRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Monopatin> obtenerMonopatinesPorKM(Float kmMinimos, Float kmMaximos){
        return monoRepo.obtenerMonopatinesPorKM(kmMinimos, kmMaximos);
    }

    @Transactional
    public List<Monopatin> obtenerMonopatinPorAnio(int x,int anio) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<Monopatin> monopatines= new ArrayList<>();
        ResponseEntity<List<Long>> response = restTemplate.exchange(
                "http://localhost:8080/vp/viajes/cantviajesxanio/" + x +"/"+ anio,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Long>>() {
                }
        );
        if(response.getStatusCode().is2xxSuccessful()){
            if (response.getBody().isEmpty()){
                throw new Exception("esta vacio");
            }
            for (Long l: response.getBody()){
                monopatines.add(this.obtenerMonopatinPorId(l));
            }
        }else{
            throw new Exception("salio mal");
        }


        return monopatines;
    }

    public List<Monopatin> obtenerMonopatinesPorUbicacion(String ubi) throws Exception {
        List<Monopatin> monopatines = monoRepo.obtenerMonmopatinesPorUbicacion(ubi);
        if(monopatines.isEmpty()){
            throw new Exception("no hay monopatines cerca");
        }
        return monopatines;
    }


    public String obtenerCantidadMonopatinesMS(){
        int cantMantenimiento = monoRepo.obtenerCantidadEnMantenimiento();
        int cantEnServicio = monoRepo.obtenerCantidadEnServicio();
        return "hay " + cantMantenimiento + " monopatines en manteniminto y " + cantEnServicio+ " en servicio";
    }

    public List<MonopatinPorUso> obtenerMonopatinPorUsoSinPausa() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<MonopatinPorUso> monopatines = new ArrayList<>();
        ResponseEntity<List<List<Integer>>> response = restTemplate.exchange(
                "http://localhost:8006/vp/viajes/monopatinid",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<List<Integer>>>() {
                }
        );
        if(response.getStatusCode().is2xxSuccessful()){
            if (response.getBody().isEmpty()){
                throw new Exception("esta vacio");
            }
            for (List<Integer> l: response.getBody()){
                monopatines.add(new MonopatinPorUso(l.get(2),l.get(0),l.get(1)));
            }
        }else{
            throw new Exception("salio mal");
        }
        return monopatines;
    }

    public List<MonopatinPorUso> obtenerMonopatinPorUsoConPausa() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<MonopatinPorUso> monopatines = new ArrayList<>();
        ResponseEntity<List<List<Integer>>> uso = restTemplate.exchange(
                "http://localhost:8006/vp/viajes/monopatinid",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<List<Integer>>>() {
                }
        );
        ResponseEntity<List<List<Integer>>> pausas = restTemplate.exchange(
                "http://localhost:8006/vp/pausas/obtenerTiempoMonopatin",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<List<Integer>>>() {
                }
        );
        if(uso.getStatusCode().is2xxSuccessful()){
            if (uso.getBody().isEmpty()){
                throw new Exception("esta vacio");
            }
            for (List<Integer> l: uso.getBody()){
                for (List<Integer> p : pausas.getBody()){
                    if(l.get(2) == p.get(2)){
                        int hour = (l.get(0) - p.get(0) <= 0 ) ? 0 : l.get(0) - p.get(0);
                        int min = (l.get(1) - p.get(1) <= 0 ) ? 0 : l.get(1) - p.get(1);
                        monopatines.add(new MonopatinPorUso(l.get(2),hour,min));
                    }
                }

            }
        }else{
            throw new Exception("salio mal");
        }
        return monopatines;
    }




}
