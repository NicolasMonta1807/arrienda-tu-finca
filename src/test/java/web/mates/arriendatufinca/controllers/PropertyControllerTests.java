package web.mates.arriendatufinca.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.controller.PropertyController;
import web.mates.arriendatufinca.dto.PropertyDTO;
import web.mates.arriendatufinca.helper.TestVariables;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.service.PropertyService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArriendatufincaApplication.class
)
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class PropertyControllerTests {
    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

    @Autowired
    private ModelMapper modelMapper;

    private final List<Property> properties = TestVariables.properties;

    @Test
    void PropertyController_CreateProperty_ReturnsPropertyDTO() {
        Property propertyToCompare = this.properties.get(0);

        PropertyDTO propertyDTO = modelMapper.map(propertyToCompare, PropertyDTO.class);

        given(propertyService.newProperty(Mockito.any(PropertyDTO.class))).willReturn(propertyDTO);

        ResponseEntity<PropertyDTO> response = propertyController.newProperty(propertyDTO);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        PropertyDTO savedProperty = response.getBody();

        Assertions.assertThat(savedProperty)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(propertyDTO);
    }

    @Test
    void PropertyController_GetAllProperties_ReturnsListPropertyDTO() {
        List<PropertyDTO> propertiesToCompare = new ArrayList<>();
        for (Property p : properties) {
            propertiesToCompare.add(modelMapper.map(p, PropertyDTO.class));
        }

        given(propertyService.getAllProperties()).willReturn(propertiesToCompare);

        ResponseEntity<List<PropertyDTO>> response = propertyController.getProperties(null);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<PropertyDTO> propertiesResponse = response.getBody();

        Assertions.assertThat(propertiesResponse).isNotNull();

        for (PropertyDTO propertyDTO : propertiesToCompare) {
            Assertions.assertThat(propertiesResponse).contains(propertyDTO);
        }
    }

    @Test
    void PropertyController_GetSinglePropertyById_ReturnsPropertyDTO() {
        Property propertyToCompare = this.properties.get(0);

        PropertyDTO expectedResult = modelMapper.map(propertyToCompare, PropertyDTO.class);

        given(propertyService.getPropertyById(Mockito.any(UUID.class))).willReturn(expectedResult);

        ResponseEntity<PropertyDTO> response = propertyController.getPropertyById(propertyToCompare.getId());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PropertyDTO property = response.getBody();

        Assertions.assertThat(property)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }

    @Test
    void PropertyController_UpdateProperty_ReturnsUpdatedPropertyDTO() {
        Property propertyToCompare = this.properties.get(1);

        String newName = "Los encaletados";
        int newRooms = 8;

        PropertyDTO previousProperty = modelMapper.map(propertyToCompare, PropertyDTO.class);
        PropertyDTO updatedProperty = modelMapper.map(propertyToCompare, PropertyDTO.class);
        updatedProperty.setName(newName);
        updatedProperty.setRooms(newRooms);

        given(propertyService.updateProperty(Mockito.any(UUID.class), Mockito.any(PropertyDTO.class))).willReturn(updatedProperty);

        ResponseEntity<PropertyDTO> response = propertyController.updateProperty(propertyToCompare.getId(), updatedProperty);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PropertyDTO propertyDTO = response.getBody();

        Assertions.assertThat(propertyDTO)
                .isNotNull()
                .isEqualTo(updatedProperty);
        Assertions.assertThat(propertyDTO.getName()).isNotEqualTo(previousProperty.getName());
        Assertions.assertThat(propertyDTO.getRooms()).isNotEqualTo(previousProperty.getRooms());
    }

    @Test
    void PropertyController_DeleteProperty_ReturnsOk() {
        UUID randomId = UUID.randomUUID();

        ResponseEntity<Void> response = propertyController.deleteProperty(randomId);
        verify(propertyService).deleteProperty(randomId);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
