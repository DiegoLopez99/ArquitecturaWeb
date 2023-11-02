package microservice.Controller;

import microservice.Model.Dto.MonopatinPorKm;
import microservice.Model.Mantenimiento;
import microservice.Service.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mantenimientos")
public class MantenimientoController {
    private final MantenimientoService manteService;
    @Autowired
    public MantenimientoController(MantenimientoService manteService) {
        this.manteService = manteService;
    }
    @GetMapping("")
    public ResponseEntity<Object> obtenerMantenimientos(){
        try {
            List<Mantenimiento> mantenimientos = manteService.obtenerMantenimientos();
            if(mantenimientos.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron mantenimientos.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(mantenimientos);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron mantenimientos.");
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> obtenerMantenimientoPorId(@PathVariable Long id){
        try {
            Mantenimiento mantenimiento = manteService.obtenerMantenimientoPorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(mantenimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el mantenimiento con ID: " + id);
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Object> guardarMantenimiento(@RequestBody Mantenimiento mantenimiento){
        try {
            Mantenimiento mantenimientoGuardado = manteService.guardarMantenimiento(mantenimiento);
            return ResponseEntity.status(HttpStatus.CREATED).body(mantenimientoGuardado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar el mantenimiento: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de mantenimiento inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el mantenimiento: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarMantenimiento(@PathVariable Long id, @RequestBody Mantenimiento mantenimientoNuevo) {
        try {
            Mantenimiento mantenimientoActualizado = manteService.actualizarMantenimiento(id, mantenimientoNuevo);
            return ResponseEntity.status(HttpStatus.OK).body(mantenimientoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de mantenimiento inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> borrarMantenimiento(@PathVariable Long id) {
        try {
            boolean mantenimientoBorrado = manteService.borrarMantenimiento(id);
            if (mantenimientoBorrado) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el mantenimiento con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de mantenimiento inválido: " + e.getMessage());
        }
    }
    @GetMapping("/obtenerReportePorKm/{max}")
    public ResponseEntity<Object> obtenerReporteMonopatinPorKm(@PathVariable  int max, @RequestBody boolean pausa){
            List<MonopatinPorKm> mantenimientoMonopatin = manteService.obtenerReporteMonopatinPorKm(max,pausa);
            if (!mantenimientoMonopatin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(mantenimientoMonopatin);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron monopatines " );
            }
    }

}
