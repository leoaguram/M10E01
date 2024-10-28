package mx.unam.dgtic.servicio;

import mx.unam.dgtic.dto.AlumnoDto;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface IAlumnoDtoService {
    public List<AlumnoDto> getAlumnosList();
    public Optional<AlumnoDto> getAlumnoById(String matricula);
    public AlumnoDto updateAlumno(AlumnoDto alumno) throws ParseException;
    public AlumnoDto createAlumno(AlumnoDto alumno) throws ParseException;
    public boolean deleteAlumno(String matricula);
    public List<AlumnoDto> findAlumnosByEstado(String estado);
}
