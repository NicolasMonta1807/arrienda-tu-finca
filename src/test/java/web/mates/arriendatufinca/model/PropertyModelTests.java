package web.mates.arriendatufinca.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.dto.PropertyDTO;
import web.mates.arriendatufinca.helper.TestVariables;

@SpringBootTest(classes = ArriendatufincaApplication.class)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class PropertyModelTests {
    @Autowired
    ModelMapper modelmapper;

    private static final Property testProperty = TestVariables.properties.get(0);
    private static PropertyDTO testPropertyDTO;

    @BeforeAll
    public static void setup() {
        testPropertyDTO = PropertyDTO.builder()
                .id(testProperty.getId())
                .name(testProperty.getName())
                .description(testProperty.getDescription())
                .rooms(testProperty.getRooms())
                .bathrooms(testProperty.getBathrooms())
                .petFriendly(testProperty.isPetFriendly())
                .pool(testProperty.isPool())
                .pricePerNight(testProperty.getPricePerNight())
                .municipalityID(testProperty.getMunicipality().getId())
                .ownerID(testProperty.getOwner().getId())
                .build();
    }

    @Test
    void PropertyModel_TestProperties() {
        Property property = Property.builder()
                .id(testProperty.getId())
                .name(testProperty.getName())
                .description(testProperty.getDescription())
                .rooms(testProperty.getRooms())
                .bathrooms(testProperty.getBathrooms())
                .petFriendly(testProperty.isPetFriendly())
                .pool(testProperty.isPool())
                .pricePerNight(testProperty.getPricePerNight())
                .municipality(testProperty.getMunicipality())
                .owner(testProperty.getOwner())
                .bookings(testProperty.getBookings())
                .build();

        Assertions.assertThat(property)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testProperty);
    }

    @Test
    void PropertyModel_TestPropertiesDTO() {
        PropertyDTO propertyDTO = modelmapper.map(testProperty, PropertyDTO.class);
        propertyDTO.setOwnerID(testProperty.getOwner().getId());
        propertyDTO.setMunicipalityID(testProperty.getMunicipality().getId());

        Assertions.assertThat(propertyDTO)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testPropertyDTO);
    }
}
