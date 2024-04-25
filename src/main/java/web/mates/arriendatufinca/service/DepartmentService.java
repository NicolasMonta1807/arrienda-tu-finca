package web.mates.arriendatufinca.service;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.mates.arriendatufinca.dto.DepartmentDTO;
import web.mates.arriendatufinca.dto.MunicipalitySimpleDTO;
import web.mates.arriendatufinca.model.Department;
import web.mates.arriendatufinca.model.Municipality;
import web.mates.arriendatufinca.repository.DepartmentRepository;

import java.util.*;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    DepartmentService(DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    public List<DepartmentDTO> getAll() {
        Iterable<Department> departments = departmentRepository.findAll();
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();

        for (Department d : departments) {
            DepartmentDTO dto = modelMapper.map(d, DepartmentDTO.class);
            Set<MunicipalitySimpleDTO> municipalitySimpleDTOS = new HashSet<>();
            for (Municipality m : d.getMunicipalities()) {
                MunicipalitySimpleDTO municipalityDTO = modelMapper.map(m, MunicipalitySimpleDTO.class);
                municipalitySimpleDTOS.add(municipalityDTO);
            }
            dto.setMunicipalities(municipalitySimpleDTOS);
            departmentDTOS.add(dto);
        }

        return departmentDTOS;
    }

    public DepartmentDTO getById(UUID id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            DepartmentDTO dto = modelMapper.map(department.get(), DepartmentDTO.class);
            Set<MunicipalitySimpleDTO> municipalitySimpleDTOS = new HashSet<>();
            for (Municipality m : department.get().getMunicipalities()) {
                MunicipalitySimpleDTO municipalityDTO = modelMapper.map(m, MunicipalitySimpleDTO.class);
                municipalitySimpleDTOS.add(municipalityDTO);
            }
            dto.setMunicipalities(municipalitySimpleDTOS);
            return dto;
        }
        return null;
    }

    public DepartmentDTO findByName(@NonNull String name) {
        Optional<Department> department = departmentRepository.findByName(name);
        if (department.isPresent())
            return modelMapper.map(department, DepartmentDTO.class);

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found");
    }

    public DepartmentDTO create(@NonNull DepartmentDTO department) {
        Department newDepartment = departmentRepository.save(
                modelMapper.map(department, Department.class)
        );
        newDepartment.setMunicipalities(new HashSet<>());
        return getById(newDepartment.getId());
    }

    public DepartmentDTO update(@NonNull UUID id, @NonNull DepartmentDTO departmentDTO) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            department.get().setName(departmentDTO.getName());
            return getById(id);
        }
        return null;
    }

    public void delete(@NonNull UUID id) {
        departmentRepository.deleteById(id);
    }
}
