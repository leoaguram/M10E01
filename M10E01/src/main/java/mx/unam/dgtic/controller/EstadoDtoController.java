package mx.unam.dgtic.controller;

import mx.unam.dgtic.dto.AlumnoDto;
import mx.unam.dgtic.dto.EstadoDto;
import mx.unam.dgtic.servicio.IEstadoDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estados")
public class EstadoDtoController {

    @Autowired
    IEstadoDtoService estadoDtoService;

   @GetMapping(path = "/")
   public List<EstadoDto> listarEstados(){
       return estadoDtoService.getEstadoList();
   }

   @GetMapping(path = "/{id}")
    public ResponseEntity<EstadoDto> getEstadoById(
            @PathVariable int id
   ){
       Optional<EstadoDto> estadoDto = estadoDtoService.getEstadoById(id);
       if (estadoDto.isPresent()){
           return ResponseEntity.ok(estadoDto.get());
       }
       return ResponseEntity.notFound().build();
   }

   @PostMapping(path = "/")
    public ResponseEntity<EstadoDto> createEstado(
            @RequestBody EstadoDto estadoDto
   ) throws ParseException, URISyntaxException {
       EstadoDto estadoDto1 = estadoDtoService.createEstado(estadoDto);
       URI location = new URI("/api/estados/" + estadoDto.getIdEstado());
       return ResponseEntity.created(location).body(estadoDto1);
   }

    @PutMapping(path = "/{id}")
    public ResponseEntity<EstadoDto> modificarEstado(
            @PathVariable int idEstado,
            @RequestBody EstadoDto estadoDto
    ) throws ParseException {
        Optional<EstadoDto> estadoDb = estadoDtoService.getEstadoById(idEstado);
        if (estadoDb.isPresent()){
            EstadoDto modificable = estadoDb.get();
            if (estadoDto.getEstado()!= null) modificable.setEstado(estadoDto.getEstado());
            if (estadoDto.getAbreviatura()!= null) modificable.setAbreviatura(estadoDto.getAbreviatura());
            return ResponseEntity.ok(estadoDtoService.updateEstado(estadoDto));
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<EstadoDto> actualizacionParcialDto(
            @PathVariable int id,
            @RequestBody EstadoDto estadoDto
    ) throws ParseException {
        Optional<EstadoDto> estadoDB = estadoDtoService.getEstadoById(id);
        if (estadoDB.isPresent()){
            EstadoDto modificable = estadoDB.get();
            if (estadoDto.getEstado() != null) modificable.setEstado(estadoDto.getEstado());
            if (estadoDto.getAbreviatura() != null) modificable.setAbreviatura(estadoDto.getAbreviatura());
            return ResponseEntity.ok(estadoDtoService.updateEstado(modificable));
        }else {
            return ResponseEntity.notFound().build();
        }
    }

   @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteEstado(
            @PathVariable int idEstado
   ){
       if (estadoDtoService.deletEstado(idEstado)){
           return ResponseEntity.ok().build();
       }else {
           return ResponseEntity.notFound().build();
       }
   }



}
