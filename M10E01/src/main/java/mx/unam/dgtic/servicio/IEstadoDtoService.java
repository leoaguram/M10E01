package mx.unam.dgtic.servicio;

import mx.unam.dgtic.dto.AlumnoDto;
import mx.unam.dgtic.dto.EstadoDto;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface IEstadoDtoService {
    public List<EstadoDto> getEstadoList();
    public Optional<EstadoDto> getEstadoById(int idEstado);
    public EstadoDto updateEstado(EstadoDto estado) throws ParseException;
    public EstadoDto createEstado(EstadoDto estado) throws ParseException;
    public boolean deletEstado(int idEstado);

}
