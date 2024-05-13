package web.mates.arriendatufinca.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.exceptions.UnauthorizedException;
import web.mates.arriendatufinca.model.department.Department;
import web.mates.arriendatufinca.model.department.dto.SimpleDepartmentDTO;
import web.mates.arriendatufinca.model.municipality.Municipality;
import web.mates.arriendatufinca.model.municipality.SimpleMunicipalityDTO;
import web.mates.arriendatufinca.repository.DepartmentRepository;
import web.mates.arriendatufinca.security.jwt.JWTFilter;

import java.util.*;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final JWTFilter jwtFilter;

    DepartmentService(DepartmentRepository departmentRepository, ModelMapper modelMapper, JWTFilter jwtFilter) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
        this.jwtFilter = jwtFilter;
    }

    public List<SimpleDepartmentDTO> getAll() {
        Iterable<Department> departments = departmentRepository.findAll();
        List<SimpleDepartmentDTO> departmentDTOS = new ArrayList<>();

        for (Department d : departments) {
            SimpleDepartmentDTO departmentDTO = modelMapper.map(d, SimpleDepartmentDTO.class);
            Set<SimpleMunicipalityDTO> municipalitySimpleDTOS = new HashSet<>();
            for (Municipality m : d.getMunicipalities()) {
                SimpleMunicipalityDTO municipalityDTO = modelMapper.map(m, SimpleMunicipalityDTO.class);
                municipalitySimpleDTOS.add(municipalityDTO);
            }
            departmentDTO.setMunicipalities(municipalitySimpleDTOS);
            departmentDTOS.add(departmentDTO);
        }

        return departmentDTOS;
    }

    public SimpleDepartmentDTO getById(@NonNull UUID id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            SimpleDepartmentDTO dto = modelMapper.map(department.get(), SimpleDepartmentDTO.class);
            Set<SimpleMunicipalityDTO> municipalitySimpleDTOS = new HashSet<>();
            for (Municipality m : department.get().getMunicipalities()) {
                SimpleMunicipalityDTO municipalityDTO = modelMapper.map(m, SimpleMunicipalityDTO.class);
                municipalitySimpleDTOS.add(municipalityDTO);
            }
            dto.setMunicipalities(municipalitySimpleDTOS);
            return dto;
        }
        throw new EntityNotFoundException("Department not found");
    }

    public SimpleDepartmentDTO findByName(@NonNull String name) {
        Optional<Department> department = departmentRepository.findByName(name);
        if (department.isPresent())
            return modelMapper.map(department, SimpleDepartmentDTO.class);

        throw new EntityNotFoundException("Department not found");
    }

    public SimpleDepartmentDTO create(@NonNull @Valid SimpleDepartmentDTO department) {
        if (!jwtFilter.isAdmin())
            throw new UnauthorizedException("Not authorized");

        Department newDepartment = departmentRepository.save(
                modelMapper.map(department, Department.class)
        );
        newDepartment.setMunicipalities(new HashSet<>());
        return getById(newDepartment.getId());
    }

    public SimpleDepartmentDTO update(@NonNull UUID id, @NonNull SimpleDepartmentDTO departmentDTO) {
        if (!jwtFilter.isAdmin())
            throw new UnauthorizedException("Not authorized");

        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            department.get().setName(departmentDTO.getName());
            return getById(id);
        }
        return null;
    }

    public void delete(@NonNull UUID id) {
        if (!jwtFilter.isAdmin())
            throw new UnauthorizedException("Not authorized");
        departmentRepository.deleteById(id);
    }
}
