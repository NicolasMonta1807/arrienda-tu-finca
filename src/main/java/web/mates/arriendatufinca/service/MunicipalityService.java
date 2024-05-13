package web.mates.arriendatufinca.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.exceptions.UnauthorizedException;
import web.mates.arriendatufinca.model.department.Department;
import web.mates.arriendatufinca.model.municipality.Municipality;
import web.mates.arriendatufinca.model.municipality.SimpleMunicipalityDTO;
import web.mates.arriendatufinca.repository.MunicipalityRepository;
import web.mates.arriendatufinca.security.jwt.JWTFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MunicipalityService {
    private final MunicipalityRepository municipalityRepository;
    private final ModelMapper modelMapper;
    private final JWTFilter jwtFilter;
    private final DepartmentService departmentService;

    MunicipalityService(MunicipalityRepository municipalityRepository, ModelMapper modelMapper, JWTFilter jwtFilter, DepartmentService departmentService) {
        this.municipalityRepository = municipalityRepository;
        this.modelMapper = modelMapper;
        this.jwtFilter = jwtFilter;
        this.departmentService = departmentService;
    }

    public List<SimpleMunicipalityDTO> getAll() {
        Iterable<Municipality> municipalities = municipalityRepository.findAll();
        List<SimpleMunicipalityDTO> municipalityDTOS = new ArrayList<>();

        for (Municipality m : municipalities) {
            SimpleMunicipalityDTO dto = modelMapper.map(m, SimpleMunicipalityDTO.class);
            dto.setDepartmentName(m.getDepartment().getName());
            municipalityDTOS.add(dto);
        }

        return municipalityDTOS;
    }

    public SimpleMunicipalityDTO getById(@NonNull UUID id) {
        Optional<Municipality> municipality = municipalityRepository.findById(id);

        if (municipality.isEmpty())
            throw new EntityNotFoundException("Municipality not found");

        SimpleMunicipalityDTO dto = modelMapper.map(municipality.get(), SimpleMunicipalityDTO.class);
        dto.setDepartmentName(municipality.get().getDepartment().getName());

        return dto;
    }

    public SimpleMunicipalityDTO create(@NonNull @Valid SimpleMunicipalityDTO municipality) {
        if (!jwtFilter.isAdmin())
            throw new UnauthorizedException();

        Municipality newMunicipality = modelMapper.map(municipality, Municipality.class);
        newMunicipality.setDepartment(modelMapper.map(departmentService.findByName(municipality.getDepartmentName()), Department.class));
        municipalityRepository.save(newMunicipality);

        return getById(newMunicipality.getId());
    }

    public SimpleMunicipalityDTO update(@NonNull UUID id, @NonNull @Valid SimpleMunicipalityDTO municipalityDTO) {
        if(!jwtFilter.isAdmin())
            throw new UnauthorizedException();

        Optional<Municipality> municipality = municipalityRepository.findById(id);
        if (municipality.isPresent()) {
            municipality.get().setName(municipalityDTO.getName());
            municipality.get().setDepartment(
                    modelMapper.map(departmentService.findByName(municipalityDTO.getDepartmentName()), Department.class)
            );
            municipalityRepository.save(municipality.get());
            return getById(id);
        }
        return null;
    }

    public void delete(@NonNull UUID id) {
        if(!jwtFilter.isAdmin())
            throw new UnauthorizedException();
        municipalityRepository.deleteById(id);
    }
}
