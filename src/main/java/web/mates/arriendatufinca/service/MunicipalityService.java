package web.mates.arriendatufinca.service;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.dto.MunicipalityDTO;
import web.mates.arriendatufinca.exceptions.DuplicateMunicipalityException;
import web.mates.arriendatufinca.model.Municipality;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.repository.MunicipalityRepository;

import java.util.*;

@Service
public class MunicipalityService {
    private final MunicipalityRepository municipalityRepository;
    private final ModelMapper modelMapper;

    MunicipalityService(MunicipalityRepository municipalityRepository, ModelMapper modelMapper) {
        this.municipalityRepository = municipalityRepository;
        this.modelMapper = modelMapper;
    }

    public List<MunicipalityDTO> getAll() {
        Iterable<Municipality> municipalities = municipalityRepository.findAll();
        List<MunicipalityDTO> municipalityDTOS = new ArrayList<>();

        for (Municipality m : municipalities) {
            municipalityDTOS.add(modelMapper.map(m, MunicipalityDTO.class));
        }

        return municipalityDTOS;
    }

    public MunicipalityDTO getById(@NonNull UUID id) {
        Optional<Municipality> municipality = municipalityRepository.findById(id);
        return municipality.map(value -> modelMapper.map(value, MunicipalityDTO.class)).orElse(null);
    }

    public MunicipalityDTO create(@NonNull MunicipalityDTO municipality) {
        if (municipalityRepository.existsByNameAndDepartment(municipality.getName(), municipality.getDepartment())) {
            throw new DuplicateMunicipalityException("Municipality already exists");
        }

        Municipality newMunicipality = municipalityRepository.save(modelMapper.map(municipality, Municipality.class));
        return getById(newMunicipality.getId());
    }

    public MunicipalityDTO update(@NonNull UUID id, @NonNull MunicipalityDTO municipalityDTO) {
        if (municipalityRepository.existsByNameAndDepartment(municipalityDTO.getName(), municipalityDTO.getDepartment())) {
            throw new DuplicateMunicipalityException("Municipality already exists");
        }

        Optional<Municipality> municipality = municipalityRepository.findById(id);
        if (municipality.isPresent()) {
            municipality.get().setName(municipalityDTO.getName());
            municipality.get().setDepartment(municipalityDTO.getDepartment());
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
