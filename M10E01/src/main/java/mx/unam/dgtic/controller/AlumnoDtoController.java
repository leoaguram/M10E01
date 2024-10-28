package mx.unam.dgtic.controller;

import mx.unam.dgtic.dto.AlumnoDto;
import mx.unam.dgtic.model.Alumno;
import mx.unam.dgtic.servicio.AlumnoDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v2/alumnos")
public class AlumnoDtoController {

    @Autowired
    AlumnoDtoService alumnoDtoService;

    @GetMapping(path = "/")
    public List<AlumnoDto> getAllDto(){
        return alumnoDtoService.getAlumnosList();
    }

    //obtener por matricula
    @GetMapping(path = "/{matricula}")
    public ResponseEntity<AlumnoDto> getByIdDto(@PathVariable String matricula){
        Optional<AlumnoDto> alumnoDto=alumnoDtoService.getAlumnoById(matricula);
        if (alumnoDto.isPresent()){
            return ResponseEntity.ok(alumnoDto.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/")
    public ResponseEntity<AlumnoDto> createAlumnoDto(@RequestBody AlumnoDto alumnoDto) throws ParseException, URISyntaxException {
        AlumnoDto alumnoDto1 =alumnoDtoService.createAlumno(alumnoDto);
        URI location =new URI("/api/v2/alumnos/" + alumnoDto1.getMatricula());
        return ResponseEntity.created(location).body(alumnoDto1);
    }

    @PutMapping(path = "/{matricula}")
    public ResponseEntity<AlumnoDto> modificarAlumno(
            @PathVariable String matricula,
            @RequestBody AlumnoDto alumnoDto
    ) throws ParseException {
        alumnoDto.setMatricula(matricula);
        AlumnoDto alumnoModificado =alumnoDtoService.updateAlumno(alumnoDto);
        return ResponseEntity.ok(alumnoModificado);
    }

    @PatchMapping(path = "/{matricula}")
    public ResponseEntity<AlumnoDto> actualizacionParcialDto(
            @PathVariable String matricula,
            @RequestBody AlumnoDto alumnoDto
    ) throws ParseException {
        Optional<AlumnoDto> alumnoDb = alumnoDtoService.getAlumnoById(matricula);
        if (alumnoDb.isPresent()){
            AlumnoDto modificable = alumnoDb.get();
            if (alumnoDto.getNombre()!= null) modificable.setNombre(alumnoDto.getNombre());
            if (alumnoDto.getPaterno()!= null) modificable.setPaterno(alumnoDto.getPaterno());
            if (alumnoDto.getFnac()!= null) modificable.setFnac(alumnoDto.getFnac());
            if (alumnoDto.getEstatura()!= 0.0) modificable.setEstatura(alumnoDto.getEstatura());
            if (alumnoDto.getEstado()!= null) modificable.setEstado(alumnoDto.getEstado());
            return ResponseEntity.ok(alumnoDtoService.updateAlumno(alumnoDto));
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path ="/{matricula}" )
    public ResponseEntity<?> aliminaAlumno(
            @PathVariable String matricula
    ){
        if (alumnoDtoService.deleteAlumno(matricula)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/estados/{edo}")
    public ResponseEntity<List<AlumnoDto>> getByEstado(@PathVariable String edo) {
        return ResponseEntity.ok(alumnoDtoService.findAlumnosByEstado(edo));
    }

    //  /api/v2/paginado?page=0&size=2&dir=asc&sort=nombre
    @GetMapping(path = "/paginado")
    public ResponseEntity<List<Alumno>> getPaginadoAlumno(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(defaultValue = "matricula") String sort
    ){
        return ResponseEntity.ok(alumnoDtoService.getAlumnosPageable(
                page,
                size,
                dir,
                sort
        ));

    }
}
