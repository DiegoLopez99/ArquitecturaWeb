package microservice.Controller;

import microservice.Model.Dto.MonopatinPorUso;
import microservice.Model.Monopatin;
import microservice.Service.MonopatinService;
import microservice.security.AuthorityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mm/monopatines")
public class MonpatinController {

    private final MonopatinService monopatinService;
    @Autowired
    public MonpatinController(MonopatinService monopatinService) {
        this.monopatinService = monopatinService;
    }

    @GetMapping("")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerMonopatines(){
        try {
            List<Monopatin> Monopatines = monopatinService.obtenerMonopatines();
            if(Monopatines.isEmpty()){
                return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron mantenimientos.");
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(Monopatines);
            }
        }catch (Exception e){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body("No se encontraron mantenimientos.");
        }
    }
    @GetMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerMonopatinesPorId(@PathVariable Long id){
        try {
            Monopatin monopatin = monopatinService.obtenerMonopatinPorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(monopatin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el monopatin con ID: " + id);
        }
    }

    @PostMapping("/guardar")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> guardarMonopatin(@RequestBody Monopatin monopatin){
        try {
            Monopatin monopatinGuardado = monopatinService.guardarMonopatin(monopatin);
            return ResponseEntity.status(HttpStatus.CREATED).body(monopatinGuardado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error de integridad de datos al guardar el monopatin: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de monopatin inválidos: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el monopatin: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> actualizarMonopatin(@PathVariable Long id, @RequestBody Monopatin monopatinNuevo) {
        try {
            Monopatin monopatinActualizado = monopatinService.actualizarMonopatin(id, monopatinNuevo);
            return ResponseEntity.status(HttpStatus.OK).body(monopatinActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos de monopatin inválidos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> borrarMonopatin(@PathVariable Long id) {
        try {
            boolean monopatinBorrado = monopatinService.borrarMonopatin(id);
            if (monopatinBorrado) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el monopatin con ID: " + id);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de monopatin inválido: " + e.getMessage());
        }
    }

    @GetMapping("reporte/kmMinimo/{kmMinimo}/kmMaximo/{kmMaximo}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerMonopatinesPorKm(@PathVariable Float kmMinimo, @PathVariable Float kmMaximo){
        List<Monopatin> monopatines = monopatinService.obtenerMonopatinesPorKM(kmMinimo, kmMaximo);

        if (!monopatines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(monopatines);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron monopatines " );
        }
    }
    @GetMapping("/cantViajes/{cantViajes}/anio/{anio}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerMonopatinesPorAnio(@PathVariable  int cantViajes, @PathVariable int anio) throws Exception {
        List<Monopatin> monopatines = monopatinService.obtenerMonopatinPorAnio(cantViajes,anio);
        if (!monopatines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(monopatines);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron monopatines " );
        }
    }
    @GetMapping("/monopatinesPorUbicacion/ubicacion/{ubi}")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerMonopatinesPorUbicacion(@PathVariable String ubi) throws Exception {
        List<Monopatin> monopatines= monopatinService.obtenerMonopatinesPorUbicacion(ubi);
        if (!monopatines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(monopatines);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron monopatines " );
        }
    }

    @GetMapping("/enOperacionYMantenimiento")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerCantidadMonopatinesMS(){
        String resultado = monopatinService.obtenerCantidadMonopatinesMS();
        if (!resultado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron monopatines " );
        }
    }
    @GetMapping("/reporte/porTiempo")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerMonopatinPorUsoSinPausa() throws Exception {
        List<MonopatinPorUso> resultado = monopatinService.obtenerMonopatinPorUsoSinPausa();
        if (!resultado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron monopatines " );
        }
    }


    @GetMapping("/reporte/porTiempoConPausa")
    @PreAuthorize( "hasAnyAuthority(\"" + AuthorityConstants.USER + "\" )" )
    public ResponseEntity<Object> obtenerMonopatinPorUsoConPausa() throws Exception {
        List<MonopatinPorUso> resultado = monopatinService.obtenerMonopatinPorUsoConPausa() ;
        if (!resultado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron monopatines " );
        }
    }

}
