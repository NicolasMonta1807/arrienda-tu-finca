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
import web.mates.arriendatufinca.dto.MunicipalityDTO;
import web.mates.arriendatufinca.helper.TestVariables;

@SpringBootTest(classes = ArriendatufincaApplication.class)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class MunicipalityModelTests {
    @Autowired
    ModelMapper modelmapper;

    private static final Municipality testMunicipality = TestVariables.municipalities.get(0);
    private static MunicipalityDTO testMunicipalityDTO;

    @BeforeAll
    public static void setup() {
        testMunicipalityDTO = MunicipalityDTO.builder()
                .id(testMunicipality.getId())
                .name(testMunicipality.getName())
                .departmentId(testMunicipality.getDepartment().getId())
                .build();
    }

    @Test
    void MunicipalityMode_TestProperties() {
        Municipality municipality = Municipality.builder()
                .id(testMunicipality.getId())
                .name(testMunicipality.getName())
                .department(testMunicipality.getDepartment())
                .properties(testMunicipality.getProperties())
                .build();

        Assertions.assertThat(municipality)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testMunicipality);
    }

    @Test
    void MunicipalityDTO_TestMapping() {
        MunicipalityDTO municipalityDTO = modelmapper.map(testMunicipality, MunicipalityDTO.class);

        Assertions.assertThat(municipalityDTO)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testMunicipalityDTO);
    }
}
