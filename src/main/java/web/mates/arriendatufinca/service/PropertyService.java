package web.mates.arriendatufinca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import web.mates.arriendatufinca.dto.PropertyDTO;
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

    PropertyService(PropertyRepository propertyRepository, UserService userService, ModelMapper modelMapper, MunicipalityService municipalityService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.municipalityService = municipalityService;
    }

    public List<PropertyDTO> getAllProperties() {
        Iterable<Property> properties = propertyRepository.findAll();
        List<PropertyDTO> propertiesDTO = new ArrayList<>();

        for (Property property : properties) {
            propertiesDTO.add(getPropertyById(property.getId()));
        }
        return propertiesDTO;
    }

    public PropertyDTO getPropertyById(@NonNull UUID id) {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isPresent()) {
            PropertyDTO propertyDTO = modelMapper.map(property, PropertyDTO.class);
            propertyDTO.setOwnerID(property.get().getOwner().getId());
            propertyDTO.setMunicipalityID(property.get().getMunicipality().getId());
            return propertyDTO;
        }
        return null;
    }

    public PropertyDTO newProperty(@NonNull PropertyDTO property) {
        Property newProperty = modelMapper.map(property, Property.class);

        newProperty.setOwner(
                modelMapper.map(
                        userService.getUserById(property.getOwnerID()),
                        User.class));

        newProperty.setMunicipality(
                modelMapper.map(
                        municipalityService.getById(property.getMunicipalityID()),
                        Municipality.class));

        Property savedProperty = propertyRepository.save(newProperty);
        return getPropertyById(savedProperty.getId());
    }

    public PropertyDTO updateProperty(@NonNull UUID id, @NonNull PropertyDTO newProperty) {
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
}
