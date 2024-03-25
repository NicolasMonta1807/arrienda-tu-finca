package web.mates.arriendatufinca.service;

import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.dto.MunicipalityDTO;
import web.mates.arriendatufinca.model.Municipality;
import web.mates.arriendatufinca.repository.MunicipalityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        municipalities.forEach(m -> municipalityDTOS.add(modelMapper.map(m, MunicipalityDTO.class)));
        return municipalityDTOS;
    }

    public MunicipalityDTO getById(@NonNull UUID id) {
        Optional<Municipality> municipality = municipalityRepository.findById(id);
        return municipality.map(value -> modelMapper.map(value, MunicipalityDTO.class)).orElse(null);
    }

    public MunicipalityDTO create(@NonNull MunicipalityDTO municipality) {
        Municipality newMunicipality = municipalityRepository.save(modelMapper.map(municipality, Municipality.class));
        return getById(newMunicipality.getId());
    }

    public MunicipalityDTO update(@NonNull UUID id, @NonNull MunicipalityDTO municipalityDTO) {
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
}
