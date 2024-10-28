package mx.unam.dgtic.servicio;

import mx.unam.dgtic.dto.AlumnoDto;
import mx.unam.dgtic.model.Alumno;
import mx.unam.dgtic.model.Estado;
import mx.unam.dgtic.repository.AlumnoRepository;
import mx.unam.dgtic.repository.EstadoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlumnoDtoService implements IAlumnoDtoService {
    @Autowired
    private AlumnoRepository alumnoRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<AlumnoDto> getAlumnosList() {
        List<Alumno> alumnos= alumnoRepository.findAll();
        return alumnos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AlumnoDto> getAlumnoById(String matricula) {
        Optional<Alumno> alumno = alumnoRepository.findById(matricula);
        if (alumno.isPresent()){
            AlumnoDto alumnoDto = convertToDto(alumno.get());
            return Optional.of(alumnoDto);
        }else {
            return Optional.empty();
        }

    }

    @Override
    public AlumnoDto updateAlumno(AlumnoDto alumno) throws ParseException {
        Alumno alumnoActualizado = alumnoRepository.save(this.convertToEntity(alumno));
        return this.convertToDto(alumnoActualizado);
    }

    @Override
    public AlumnoDto createAlumno(AlumnoDto alumno) throws ParseException {
        Alumno alumnoSalvado=alumnoRepository.save(this.convertToEntity(alumno));
        return convertToDto(alumnoSalvado);
    }

    @Override
    public boolean deleteAlumno(String matricula) {
        Optional<Alumno> alumno =alumnoRepository.findById(matricula);
        if (alumno.isPresent()){
            alumnoRepository.deleteById(matricula);
            return true;
        }else {
            return false;
        }
        //return alumnoRepository.deleteById(matricula);
    }

    @Override
    public List<AlumnoDto> findAlumnosByEstado(String estado) {
        List<Alumno> alumnos= alumnoRepository.findByEstadoEstado(estado);
        return alumnos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AlumnoDto convertToDto(Alumno alumno){
        AlumnoDto alumnoDto = modelMapper.map(alumno,AlumnoDto.class);
        if (alumno.getEstado() != null)
            alumnoDto.setEstado(alumno.getEstado().getEstado());
        if (alumno.getFnac() != null){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fnacStr = dateFormat.format(alumno.getFnac());
            alumnoDto.setFnac(fnacStr);
        }
        return alumnoDto;

    }

    private Alumno convertToEntity(AlumnoDto alumnoDto) throws ParseException {
        Alumno alumno = modelMapper.map(alumnoDto, Alumno.class);
        if (alumnoDto.getEstado() != null && !alumnoDto.getEstado().isEmpty()){
            Estado estado = estadoRepository.findByEstado(alumnoDto.getEstado());
            alumno.setEstado(estado);
        }
        if (alumnoDto.getFnac() != null && alumnoDto.getFnac().isEmpty() && !alumnoDto.getFnac().isBlank()){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fnacDate = dateFormat.parse(alumnoDto.getFnac());
            alumno.setFnac(fnacDate);
        }else {
            alumno.setFnac(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
        }
        return alumno;
    }

    public List<Alumno> getAlumnosPageable(int page,
                                              int size,
                                              String dirSort,
                                              String sort){
        PageRequest pageRequest =PageRequest.of(page, size,
                Sort.Direction.fromString(dirSort),sort);
        Page<Alumno> pageResult = alumnoRepository.findAll(pageRequest);
        return pageResult.stream().toList();
    }
}
