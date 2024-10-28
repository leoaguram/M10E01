package mx.unam.dgtic.servicio;

import mx.unam.dgtic.dto.EstadoDto;
import mx.unam.dgtic.model.Estado;
import mx.unam.dgtic.repository.EstadoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class EstadoDtoService implements IEstadoDtoService{
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public List<EstadoDto> getEstadoList() {
        List<Estado> estados = estadoRepository.findAll();
        return estados.stream().map(this::toDto).toList();
    }

    @Override
    public Optional<EstadoDto> getEstadoById(int idEstado) {
        Optional<Estado> estado = estadoRepository.findById(idEstado);
        return estado.map(this::toDto);
    }

    @Override
    public EstadoDto updateEstado(EstadoDto estado) throws ParseException {
        Estado estadoActualizado = estadoRepository.save(this.toEntity(estado));
          return toDto(estadoActualizado);
    }

    @Override
    public EstadoDto createEstado(EstadoDto estado) throws ParseException {
        Estado estadoCreado = estadoRepository.save(this.toEntity(estado));
        return toDto(estadoCreado);
    }

    @Override
    public boolean deletEstado(int idEstado) {
        if (estadoRepository.existsById(idEstado)){
            estadoRepository.deleteById(idEstado);
            return true;
        }
        return false;
    }

    private EstadoDto toDto(Estado estado){
        return modelMapper.map(estado, EstadoDto.class);
    }

    private Estado toEntity(EstadoDto estado){
        return modelMapper.map(estado, Estado.class);
    }

}
