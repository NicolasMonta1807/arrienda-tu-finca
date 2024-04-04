package web.mates.arriendatufinca.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.dto.MunicipalityDTO;
import web.mates.arriendatufinca.dto.PropertyDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.helper.TestVariables;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.repository.PropertyRepository;
import web.mates.arriendatufinca.service.MunicipalityService;
import web.mates.arriendatufinca.service.PropertyService;
import web.mates.arriendatufinca.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArriendatufincaApplication.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class PropertyServiceTests {
    @Mock
    PropertyRepository propertyRepository;

    @Mock
    UserService userService;

    @Mock
    MunicipalityService municipalityService;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    PropertyService propertyService;

    private final List<Property> properties = TestVariables.properties;

    @Test
    void PropertyService_CreateProperty_ReturnsPropertyDTO() {
        Property baseProperty = properties.get(0);

        PropertyDTO propertyDTO = modelMapper.map(baseProperty, PropertyDTO.class);
        propertyDTO.setOwnerID(baseProperty.getOwner().getId());
        propertyDTO.setMunicipalityID(baseProperty.getMunicipality().getId());

        UserDTO ownerDTO = modelMapper.map(baseProperty.getOwner(), UserDTO.class);

        MunicipalityDTO municipalityDTO = modelMapper.map(baseProperty.getMunicipality(), MunicipalityDTO.class);

        when(userService.getUserById(any(UUID.class))).thenReturn(ownerDTO);
        when(municipalityService.getById(any(UUID.class))).thenReturn(municipalityDTO);
        when(propertyRepository.save(any(Property.class))).thenReturn(baseProperty);
        when(propertyRepository.findById(any(UUID.class))).thenReturn(Optional.of(baseProperty));

        // When
        PropertyDTO savedProperty = propertyService.newProperty(propertyDTO);

        Assertions.assertThat(savedProperty)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(propertyDTO);

        verify(propertyRepository, times(1)).save(any(Property.class));
    }

    @Test
    void PropertyService_GetAllProperties_ReturnsListPropertyDTO() {
        // Given
        when(propertyRepository.findAll()).thenReturn(properties);
        when(propertyRepository.findById(properties.get(0).getId())).thenReturn(Optional.ofNullable(properties.get(0)));
        when(propertyRepository.findById(properties.get(1).getId())).thenReturn(Optional.ofNullable(properties.get(1)));

        // When
        List<PropertyDTO> response = propertyService.getAllProperties();

        // Then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.get(0).getId()).isEqualTo(properties.get(0).getId());
        Assertions.assertThat(response.get(1).getId()).isEqualTo(properties.get(1).getId());
        verify(propertyRepository, times(2)).findById(any(UUID.class));
    }

    @Test
    void PropertyService_GetPropertyById_ReturnsPropertyDTO() {
        // Given
        Property baseProperty = properties.get(0);
        UUID id = baseProperty.getId();
        when(propertyRepository.findById(any(UUID.class))).thenReturn(Optional.of(baseProperty));

        PropertyDTO expected = modelMapper.map(baseProperty, PropertyDTO.class);

        // When
        PropertyDTO propertyDTO = propertyService.getPropertyById(id);

        // Then
        Assertions.assertThat(propertyDTO)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void PropertyService_UpdateProperty_ReturnsUpdatedPropertyDTO() {
        // Given
        Property baseProperty = properties.get(0);
        UUID id = baseProperty.getId();

        String newName = "El cielo";
        Property updated = properties.get(0);
        updated.setName(newName);

        when(propertyRepository.findById(any(UUID.class))).thenReturn(Optional.of(baseProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(updated);

        PropertyDTO expected = modelMapper.map(updated, PropertyDTO.class);

        // When
        PropertyDTO newProperty = propertyService.updateProperty(id, expected);

        // Then
        Assertions.assertThat(newProperty)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
        Assertions.assertThat(newProperty.getName()).isEqualTo(newName);
        verify(propertyRepository, times(1)).save(any(Property.class));
    }

    @Test
    void PropertyService_DeletesProperty_ReturnsVoidAndDeletesProperty() {
        // Given
        Property propertyToUse = properties.get(0);
        when(propertyRepository.findById(any(UUID.class))).thenReturn(Optional.of(propertyToUse));

        // When
        propertyService.deleteProperty(propertyToUse.getId());

        // Then
        verify(propertyRepository, times(1)).deleteById(propertyToUse.getId());
    }
}
