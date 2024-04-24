package web.mates.arriendatufinca.service;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.dto.MunicipalityDTO;
import web.mates.arriendatufinca.dto.MunicipalityInfoDTO;
import web.mates.arriendatufinca.model.Department;
import web.mates.arriendatufinca.model.Municipality;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.repository.MunicipalityRepository;

import java.util.*;

@Service
public class MunicipalityService {
    private final MunicipalityRepository municipalityRepository;
    private final DepartmentService departmentService;
    private final ModelMapper modelMapper;

    MunicipalityService(MunicipalityRepository municipalityRepository, ModelMapper modelMapper, DepartmentService departmentService) {
        this.municipalityRepository = municipalityRepository;
        this.modelMapper = modelMapper;
        this.departmentService = departmentService;
    }

    public List<MunicipalityInfoDTO> getAll() {
        Iterable<Municipality> municipalities = municipalityRepository.findAll();
        List<MunicipalityInfoDTO> municipalityDTOS = new ArrayList<>();

        for (Municipality m : municipalities) {
            municipalityDTOS.add(getById(m.getId()));
        }

        return municipalityDTOS;
    }

    public MunicipalityInfoDTO getById(@NonNull UUID id) {
        Optional<Municipality> municipality = municipalityRepository.findById(id);
        if (municipality.isPresent()) {
            MunicipalityInfoDTO municipalityDTO = modelMapper.map(municipality, MunicipalityInfoDTO.class);
            municipalityDTO.setDepartmentName(municipality.get().getDepartment().getName());
            return municipalityDTO;
        }
        return null;
    }

    public MunicipalityInfoDTO create(@NonNull MunicipalityDTO municipality) {
        Municipality newMunicipality = modelMapper.map(municipality, Municipality.class);

        newMunicipality.setDepartment(
                modelMapper.map(departmentService.getById(municipality.getDepartmentId()), Department.class)
        );
        municipalityRepository.save(newMunicipality);
        System.out.println("NEW MUNI: " + newMunicipality.getName() + " - " + newMunicipality.getDepartment());
        return getById(newMunicipality.getId());
    }

    public MunicipalityInfoDTO update(@NonNull UUID id, @NonNull MunicipalityDTO municipalityDTO) {
        Optional<Municipality> municipality = municipalityRepository.findById(id);
        if (municipality.isPresent()) {
            municipality.get().setName(municipalityDTO.getName());
            municipality.get().setDepartment(
                    modelMapper.map(departmentService.getById(municipalityDTO.getDepartmentId()), Department.class)
            );
            municipalityRepository.save(municipality.get());
            return getById(id);
        }
        return null;
    }

    public void delete(@NonNull UUID id) {
        municipalityRepository.deleteById(id);
    }

    public void removeProperty(@NonNull UUID municipalityId, @NonNull Property property) {
        Optional<Municipality> municipality = municipalityRepository.findById(municipalityId);
        if (municipality.isPresent()) {
            Set<Property> properties = municipality.get().getProperties();
            properties.remove(property);
            municipality.get().setProperties(properties);
            municipalityRepository.save(municipality.get());
        }
    }
}
