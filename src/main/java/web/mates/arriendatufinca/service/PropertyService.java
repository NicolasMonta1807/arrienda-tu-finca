package web.mates.arriendatufinca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import web.mates.arriendatufinca.dto.PropertyDTO;
import web.mates.arriendatufinca.dto.PropertyInfoDTO;
import web.mates.arriendatufinca.model.Department;
import web.mates.arriendatufinca.model.Municipality;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.repository.PropertyRepository;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MunicipalityService municipalityService;
    private final DepartmentService departmentService;

    PropertyService(PropertyRepository propertyRepository, UserService userService, ModelMapper modelMapper,
            MunicipalityService municipalityService, DepartmentService departmentService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.municipalityService = municipalityService;
        this.departmentService = departmentService;
    }

    public List<PropertyInfoDTO> getAllProperties() {
        Iterable<Property> properties = propertyRepository.findAll();
        List<PropertyInfoDTO> propertiesDTO = new ArrayList<>();

        for (Property property : properties) {
            propertiesDTO.add(getPropertyById(property.getId()));
        }
        return propertiesDTO;
    }

    public PropertyInfoDTO getPropertyById(@NonNull UUID id) {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isPresent()) {
            PropertyInfoDTO propertyDTO = modelMapper.map(property, PropertyInfoDTO.class);
            propertyDTO.setOwnerID(property.get().getOwner().getId());
            propertyDTO.setMunicipalityName(property.get().getMunicipality().getName());
            propertyDTO.setDepartmentName(property.get().getMunicipality().getDepartment().getName());
            return propertyDTO;
        }
        return null;
    }

    public PropertyInfoDTO newProperty(@NonNull PropertyDTO property) {
        Property newProperty = modelMapper.map(property, Property.class);

        newProperty.setOwner(
                modelMapper.map(
                        userService.getUserById(property.getOwnerID()),
                        User.class));

        newProperty.setMunicipality(
                modelMapper.map(
                        municipalityService.getById(property.getMunicipalityID()),
                        Municipality.class));

        newProperty.getMunicipality().setDepartment(
                modelMapper.map(
                        departmentService.findByName(
                                municipalityService.getById(property.getMunicipalityID()).getDepartmentName()),
                        Department.class));

        Property savedProperty = propertyRepository.save(newProperty);
        return getPropertyById(savedProperty.getId());
    }

    public PropertyInfoDTO updateProperty(@NonNull UUID id, @NonNull PropertyDTO newProperty) {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isPresent()) {
            property.get().setName(newProperty.getName());
            property.get().setDescription(newProperty.getDescription());
            property.get().setBathrooms(newProperty.getBathrooms());
            property.get().setRooms(newProperty.getRooms());
            property.get().setPricePerNight(newProperty.getPricePerNight());
            property.get().setBbq(newProperty.isBbq());
            property.get().setPetFriendly(newProperty.isPetFriendly());
            property.get().setPool(newProperty.isPool());

            property.get().setMunicipality(
                    modelMapper.map(municipalityService.getById(newProperty.getMunicipalityID()), Municipality.class));
            property.get().getMunicipality().setDepartment(
                    modelMapper.map(
                            departmentService.findByName(
                                    municipalityService.getById(newProperty.getMunicipalityID()).getDepartmentName()),
                            Department.class));

            propertyRepository.save(property.get());
            return getPropertyById(property.get().getId());
        } else
            return null;
    }

    public void deleteProperty(@NonNull UUID id) {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isPresent()) {
            userService.removeProperty(property.get().getOwner().getId(), property.get());
            municipalityService.removeProperty(property.get().getMunicipality().getId(), property.get());
            propertyRepository.deleteById(id);
        }
    }

    public List<PropertyInfoDTO> getPropertiesFromOwner(@NonNull UUID id) {
        Iterable<Property> properties = propertyRepository.findByOwner(
                modelMapper.map(userService.getUserById(id), User.class));
        List<PropertyInfoDTO> propertiesDTO = new ArrayList<>();

        if (properties == null)
            return null;

        for (Property p : properties)
            propertiesDTO.add(getPropertyById(p.getId()));
        return propertiesDTO;
    }

    public List<PropertyInfoDTO> findProperties(UUID municipality, String name) {
        if (municipality == null && name == null)
            return getAllProperties();

        Municipality municipalityObject = null;
        if (municipality != null)
            municipalityObject = modelMapper.map(
                    municipalityService.getById(municipality),
                    Municipality.class);

        Iterable<Property> properties = propertyRepository.findByMunicipalityOrName(municipalityObject, name);

        if (properties == null)
            throw new EntityNotFoundException("No property found");

        List<PropertyInfoDTO> propertyDTOS = new ArrayList<>();
        for (Property p : properties)
            propertyDTOS.add(getPropertyById(p.getId()));
        return propertyDTOS;
    }
}
