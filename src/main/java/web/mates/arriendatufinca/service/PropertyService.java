package web.mates.arriendatufinca.service;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.exceptions.EntityNotFoundException;
import web.mates.arriendatufinca.exceptions.UnauthorizedException;
import web.mates.arriendatufinca.model.department.Department;
import web.mates.arriendatufinca.model.municipality.Municipality;
import web.mates.arriendatufinca.model.property.Property;
import web.mates.arriendatufinca.model.property.dto.NewPropertyDTO;
import web.mates.arriendatufinca.model.property.dto.SimplePropertyDTO;
import web.mates.arriendatufinca.model.user.User;
import web.mates.arriendatufinca.repository.PropertyRepository;
import web.mates.arriendatufinca.security.jwt.JWTFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;
    private final JWTFilter jwtFilter;
    private final UserService userService;
    private final MunicipalityService municipalityService;
    private final DepartmentService departmentService;

    private void checkAuth(String emailToCheck) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getName().equalsIgnoreCase(emailToCheck))
            throw new UnauthorizedException("Not authorized");
    }

    PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, JWTFilter jwtFilter, UserService userService, MunicipalityService municipalityService, DepartmentService departmentService) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper;
        this.jwtFilter = jwtFilter;
        this.userService = userService;
        this.municipalityService = municipalityService;
        this.departmentService = departmentService;
    }

    public List<SimplePropertyDTO> getAll(boolean exclude) {
        UUID authId = jwtFilter.getAuthId();
        Iterable<Property> properties = propertyRepository.findAll();
        List<SimplePropertyDTO> propertyDTOS = new ArrayList<>();

        for (Property p : properties) {
            SimplePropertyDTO propertyDTO = modelMapper.map(p, SimplePropertyDTO.class);
            propertyDTO.setMunicipalityName(p.getMunicipality().getName());
            propertyDTO.setDepartmentName(p.getMunicipality().getDepartment().getName());
            if (!exclude || !authId.equals(p.getOwner().getId()))
                propertyDTOS.add(propertyDTO);
        }
        return propertyDTOS;
    }

    public SimplePropertyDTO getById(@NonNull UUID id) {
        Optional<Property> foundProperty = propertyRepository.findById(id);

        if (foundProperty.isEmpty())
            throw new EntityNotFoundException("Property not found");

        Property property = foundProperty.get();

        SimplePropertyDTO propertyDTO = modelMapper.map(property, SimplePropertyDTO.class);
        propertyDTO.setMunicipalityName(property.getMunicipality().getName());
        propertyDTO.setDepartmentName(property.getMunicipality().getDepartment().getName());

        return propertyDTO;
    }

    public SimplePropertyDTO create(@NonNull @Valid NewPropertyDTO property) {
        Property newProperty = modelMapper.map(property, Property.class);
        User owner = modelMapper.map(
                userService.getById(jwtFilter.getAuthId()),
                User.class
        );

        Municipality municipality = modelMapper.map(
                municipalityService.getById(property.getMunicipalityId()),
                Municipality.class
        );

        newProperty.setOwner(owner);
        newProperty.setMunicipality(municipality);
        newProperty.getMunicipality().setDepartment(
                modelMapper.map(
                        departmentService.findByName(
                                municipalityService.getById(property.getMunicipalityId()).getDepartmentName()),
                        Department.class));

        Property saved = propertyRepository.save(newProperty);
        return getById(saved.getId());
    }

    public SimplePropertyDTO update(@NonNull UUID id, @NonNull @Valid NewPropertyDTO property) {
        Optional<Property> foundProperty = propertyRepository.findById(id);

        if (foundProperty.isEmpty())
            throw new EntityNotFoundException("Property not found");

        checkAuth(foundProperty.get().getOwner().getEmail());

        Property propertyToUpdate = foundProperty.get();
        propertyToUpdate.setName(property.getName());
        propertyToUpdate.setDescription(property.getDescription());
        propertyToUpdate.setRooms(property.getRooms());
        propertyToUpdate.setBathrooms(property.getBathrooms());
        propertyToUpdate.setPetFriendly(property.isPetFriendly());
        propertyToUpdate.setPool(property.isPool());
        propertyToUpdate.setBbq(property.isBbq());
        propertyToUpdate.setPricePerNight(property.getPricePerNight());

        if (!property.getMunicipalityId().equals(propertyToUpdate.getMunicipality().getId())) {
            Municipality municipality = modelMapper.map(
                    municipalityService.getById(property.getMunicipalityId()),
                    Municipality.class
            );

            propertyToUpdate.setMunicipality(municipality);

            propertyToUpdate.getMunicipality().setDepartment(
                    modelMapper.map(
                            departmentService.findByName(
                                    municipalityService.getById(property.getMunicipalityId()).getDepartmentName()),
                            Department.class));
        }

        propertyRepository.save(propertyToUpdate);
        return getById(propertyToUpdate.getId());
    }

    public void delete(@NonNull UUID id) {
        Optional<Property> propertyToDelete = propertyRepository.findById(id);

        if (propertyToDelete.isEmpty())
            return;

        checkAuth(propertyToDelete.get().getOwner().getEmail());
        propertyRepository.deleteById(id);
    }

    public List<SimplePropertyDTO> finder(UUID municipality, String name) {
        if (municipality == null && name == null)
            return getAll(true);

        Municipality municipalityObject = null;
        if (municipality != null)
            municipalityObject = modelMapper.map(
                    municipalityService.getById(municipality),
                    Municipality.class);

        Iterable<Property> properties = propertyRepository.findByMunicipalityOrName(municipalityObject, name);

        if (properties == null)
            throw new EntityNotFoundException("No property found");

        UUID authId = jwtFilter.getAuthId();
        List<SimplePropertyDTO> propertyDTOS = new ArrayList<>();
        for (Property p : properties)
            if (!p.getOwner().getId().equals(authId))
                propertyDTOS.add(getById(p.getId()));
        return propertyDTOS;
    }

    public List<SimplePropertyDTO> getFromOwner() {
        UUID authId = jwtFilter.getAuthId();
        User owner = modelMapper.map(userService.getById(authId), User.class);
        Iterable<Property> properties = propertyRepository.findByOwner(owner);
        List<SimplePropertyDTO> propertyDTOS = new ArrayList<>();
        for (Property p : properties)
            propertyDTOS.add(getById(p.getId()));

        if (propertyDTOS.isEmpty())
            throw new EntityNotFoundException("No properties found");

        return propertyDTOS;
    }
}
