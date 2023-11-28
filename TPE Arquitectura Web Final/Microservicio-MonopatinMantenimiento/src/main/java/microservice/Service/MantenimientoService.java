package microservice.Service;

import microservice.Model.Dto.MonopatinPorKm;
import microservice.Model.Mantenimiento;
import microservice.Model.Monopatin;
import microservice.Repository.MantenimientoRepository;
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
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
@Service
public class MantenimientoService {
    private MantenimientoRepository manteRepo;
    private RestTemplate restTemplate;
    private MonopatinService monopatinService;
    @Autowired
    public MantenimientoService(MantenimientoRepository m, RestTemplate restTemplate, MonopatinService monopatinService){
        manteRepo = m;
        this.restTemplate = restTemplate;
        this.monopatinService = monopatinService;
    }

    public List<Mantenimiento> obtenerMantenimientos(){
        return manteRepo.findAll();
    }

    public Mantenimiento obtenerMantenimientoPorId(Long id){
        Optional<Mantenimiento> man = manteRepo.findById(id);
        if (man.isPresent()){
            return man.get();
        }else{
            throw new RuntimeException("No se encontró la mantenimiento con ID: " + id);
        }
    }

    @Transactional
    public Mantenimiento guardarMantenimiento(Mantenimiento mantenimiento){
        try {
            return manteRepo.save(mantenimiento);
        }catch (DataAccessException e) {
            throw new DataIntegrityViolationException("Error de integridad de datos al guardar el mantenimiento: " + e.getMessage(), e);
        }
    }
    @Transactional
    public Mantenimiento actualizarMantenimiento(Long id, Mantenimiento mantenimiento){
        Optional<Mantenimiento> mantenimientoExistente = manteRepo.findById(id);
        if (mantenimientoExistente.isPresent()){
            Mantenimiento mantenimiento1 = mantenimientoExistente.get();
            mantenimiento1.setInicio(mantenimiento.getInicio());
            mantenimiento1.setFinalizacion(mantenimiento.getFinalizacion());
            mantenimiento1.setMotivo(mantenimiento.getMotivo());
            mantenimiento1.setMonopatin(mantenimiento.getMonopatin());
            return manteRepo.save(mantenimiento1);
        }else{
            throw new RuntimeException("No se encontró el mantenimiento con ID: " + id);
        }
    }

    @Transactional
    public boolean borrarMantenimiento(Long id){
        Optional<Mantenimiento> mantenimiento = manteRepo.findById(id);
        if (mantenimiento.isPresent()) {
            manteRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<MonopatinPorKm> obtenerReporteMonopatinPorKm(int max, boolean pausa){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<Monopatin> monpatines = monopatinService.obtenerMonopatines();
        monpatines.sort(Comparator.comparing(Monopatin::getKm_recorridos).reversed());
        List<MonopatinPorKm> monopatinPorKms = new ArrayList<>();
        ResponseEntity<List<List<Integer>>> pausas = restTemplate.exchange(
                "http://localhost:8080/vp/pausas/obtenerTiempoMonopatin",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<List<Integer>>>() {
                }
        );
        for (Monopatin m: monpatines){
            for (List<Integer> p: pausas.getBody()){
                if(m.getId().intValue() == p.get(2)){
                    int hora = (pausa) ? p.get(0) : 0;
                    int min = (pausa) ? p.get(1) : 0;
                    boolean reparacion = (m.getKm_recorridos() >= max) ? true : false;
                    monopatinPorKms.add(new MonopatinPorKm(m.getId(),hora,min,reparacion));
                }
            }
        }
        return monopatinPorKms;
    }
}
