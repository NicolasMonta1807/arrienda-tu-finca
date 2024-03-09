package web.mates.arriendatufinca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import web.mates.arriendatufinca.dto.PropertyDTO;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.repository.PropertyRepository;

@Service
public class PropertyService {

  @Autowired
  PropertyRepository propertyRepository;

  @Autowired
  UserService userService;

  @Autowired
  ModelMapper modelMapper;

  PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper) {
    this.propertyRepository = propertyRepository;
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
    Property property = propertyRepository.findById(id).get();
    return modelMapper.map(property, PropertyDTO.class);
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
    Property property = propertyRepository.findById(id).get();
    property.setName(newProperty.getName());
    property.setDescription(newProperty.getDescription());
    property.setBathrooms(newProperty.getBathrooms());
    property.setRooms(newProperty.getRooms());
    property.setPricePerNight(newProperty.getPricePerNight());
    property.setBbq(newProperty.isBbq());
    property.setPetFriendly(newProperty.isPetFriendly());
    property.setPool(newProperty.isPool());
    propertyRepository.save(property);
    return getPropertyById(property.getId());
  }

  public Boolean deleteProperty(@NonNull UUID id) {
    if (propertyRepository.existsById(id)) {
      propertyRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
