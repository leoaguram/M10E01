package mx.unam.dgtic.controller;

import mx.unam.dgtic.dto.AlumnoDto;
import mx.unam.dgtic.model.Alumno;
import mx.unam.dgtic.repository.AlumnoRepository;
import mx.unam.dgtic.servicio.AlumnoDtoService;
import mx.unam.dgtic.servicio.IAlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping (path="api/alumnos",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AlumnoController {

    @Autowired
    private IAlumnoService alumnoService;

    @GetMapping(path = "/")
    public ResponseEntity<List<Alumno>> getAll(){
        return ResponseEntity.ok(alumnoService.getAlumnosList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Alumno> getById(@PathVariable String id){
        Optional<Alumno> alumno = alumnoService.getAlumnoById(id);
        if (alumno.isPresent()) {
            return ResponseEntity.ok(alumno.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> deleteAlumno(@PathVariable String id){
        if (alumnoService.deleteAlumno(id)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path ="/")
    public ResponseEntity<Alumno> createAlumno(@RequestBody Alumno alumno) throws URISyntaxException {
        Alumno alumnoNuevo=alumnoService.createAlumno(alumno);
        URI location = new URI("/api/alumnos/" + alumnoNuevo.getMatricula() );
        return ResponseEntity.created(location).body(alumnoNuevo);
    }

    @PostMapping(path = "/{id}")
    public ResponseEntity<Alumno> updateAlumno(
            @PathVariable String id,
            @RequestBody Alumno alumno
    ) throws URISyntaxException {
        Optional<Alumno> alumnoDb = alumnoService.getAlumnoById(id);
        if (alumnoDb.isPresent()){
            return ResponseEntity.ok(alumnoService.updateAlumno(alumno));
        }else {
            Alumno alumnoNuevo = alumnoService.createAlumno(alumno);
            URI location = new URI("/api/alumnos/" + alumnoNuevo.getMatricula());
            return ResponseEntity.created(location).body(alumnoNuevo);
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Alumno> actualizacionParcial(
            @PathVariable String id,
            @RequestBody Alumno alumno
        ){
        Optional<Alumno> alumnoToUpdate = alumnoService.getAlumnoById(id);
        if (alumnoToUpdate.isPresent()){
            Alumno alumnoParcial=alumnoToUpdate.get();
            if (alumno.getNombre() != null) alumnoParcial.setNombre(alumno.getNombre());
            if (alumno.getPaterno() != null) alumnoParcial.setPaterno(alumno.getPaterno());
            if (alumno.getFnac() != null) alumnoParcial.setFnac(alumno.getFnac());
            if (alumno.getEstatura() != 0.0) alumnoParcial.setEstatura(alumno.getEstatura());
            return ResponseEntity.ok(alumnoService.updateAlumno(alumnoParcial));

        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/estados/{edo}")
    public ResponseEntity<List<Alumno>> getByEstado(
            @PathVariable String edo
    ){
        return ResponseEntity.ok(alumnoService.findAlumnosByEstado(edo));
    }


}
