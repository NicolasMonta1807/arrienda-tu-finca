package web.mates.arriendatufinca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import web.mates.arriendatufinca.dto.PropertyDTO;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.repository.PropertyRepository;

import javax.swing.text.html.Option;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    PropertyService(PropertyRepository propertyRepository, UserService userService, ModelMapper modelMapper) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    public List<PropertyDTO> getAllProperties() {
        Iterable<Property> properties = propertyRepository.findAll();
        List<PropertyDTO> propertiesDTO = new ArrayList<>();

        for (Property property : properties) {
            propertiesDTO.add(modelMapper.map(property, PropertyDTO.class));
        }
        return propertiesDTO;
    }

    public PropertyDTO getPropertyById(@NonNull UUID id) {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isPresent())
            return modelMapper.map(property, PropertyDTO.class);
        return null;
    }

    public PropertyDTO newProperty(@NonNull PropertyDTO property) {
        Property newProperty = modelMapper.map(property, Property.class);

        newProperty.setOwner(
                modelMapper.map(
                        userService.getUserById(property.getOwnerID()),
                        User.class));

        propertyRepository.save(newProperty);
        return getPropertyById(newProperty.getId());
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

    public Boolean deleteProperty(@NonNull UUID id) {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isPresent()) {
            userService.removeProperty(property.get().getOwner().getId(), property.get());
            propertyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
