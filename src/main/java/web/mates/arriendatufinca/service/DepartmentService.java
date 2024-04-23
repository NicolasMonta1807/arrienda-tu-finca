package web.mates.arriendatufinca.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.dto.DepartmentDTO;
import web.mates.arriendatufinca.dto.MunicipalitySimpleDTO;
import web.mates.arriendatufinca.model.Department;
import web.mates.arriendatufinca.model.Municipality;
import web.mates.arriendatufinca.repository.DepartmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            for (Municipality m : d.getMunicipalities()) {
                dto.getMunicipalities().add(modelMapper.map(m, MunicipalitySimpleDTO.class));
            }
            departmentDTOS.add(dto);
        }
        return departmentDTOS;
    }

    public DepartmentDTO getById(UUID id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            DepartmentDTO dto = modelMapper.map(department.get(), DepartmentDTO.class);
            for (Municipality m : department.get().getMunicipalities()) {
                dto.getMunicipalities().add(modelMapper.map(m, MunicipalitySimpleDTO.class));
            }
            return dto;
        }
        return null;
    }
}
