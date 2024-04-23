package web.mates.arriendatufinca.controllers;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
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
import web.mates.arriendatufinca.controller.MunicipalityController;
import web.mates.arriendatufinca.dto.MunicipalityDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.helper.TestVariables;
import web.mates.arriendatufinca.model.Municipality;
import web.mates.arriendatufinca.service.MunicipalityService;

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
class MunicipalityControllerTests {
    @Mock
    private MunicipalityService municipalityService;

    @InjectMocks
    private MunicipalityController municipalityController;

    @Autowired
    private ModelMapper modelMapper;

    private final List<Municipality> municipalities = TestVariables.municipalities;

    @Test
    void MunicipalityController_CreateMunicipality_ReturnsMunicipalityDTO() {
        Municipality municipalityToCompare = this.municipalities.get(0);

        MunicipalityDTO municipalityDTO = modelMapper.map(municipalityToCompare, MunicipalityDTO.class);

        given(municipalityService.create(Mockito.any(MunicipalityDTO.class))).willReturn(municipalityDTO);

        ResponseEntity<MunicipalityDTO> response = municipalityController.createMunicipality(municipalityDTO);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        MunicipalityDTO savedMunicipality = response.getBody();

        Assertions.assertThat(savedMunicipality)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(municipalityDTO);
    }

    @Test
    void MunicipalityController_GetAllMunicipalities_ReturnsListMunicipalityDTO() {
        List<MunicipalityDTO> municipalitiesToCompare = new ArrayList<>();
        for (Municipality m : municipalities) {
            municipalitiesToCompare.add(modelMapper.map(m, MunicipalityDTO.class));
        }

        given(municipalityService.getAll()).willReturn(municipalitiesToCompare);

        ResponseEntity<List<MunicipalityDTO>> response = municipalityController.getAllMunicipalities();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<MunicipalityDTO> municipalitiesResponse = response.getBody();

        Assertions.assertThat(municipalitiesResponse).isNotNull();

        for (MunicipalityDTO municipalityDTO : municipalitiesToCompare) {
            Assertions.assertThat(municipalitiesResponse).contains(municipalityDTO);
        }
    }

    @Test
    void MunicipalityController_GetMunicipality__ReturnsMunicipalityDTO() {
        Municipality municipalityToCompare = this.municipalities.get(0);

        MunicipalityDTO municipalityDTO = MunicipalityDTO.builder()
                .name(municipalityToCompare.getName())
                .id(municipalityToCompare.getId())
                .departmentId(municipalityToCompare.getDepartment().getId())
                .build();

        given(municipalityService.getById(Mockito.any(UUID.class))).willReturn(municipalityDTO);

        ResponseEntity<MunicipalityDTO> response = municipalityController.getMunicipalityById(municipalityToCompare.getId());
        MunicipalityDTO municipalityResponse = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void MunicipalityController_UpdateMunicipality_ReturnsUpdatedMunicipality() {
        Municipality municipalityToCompare = this.municipalities.get(0);

        String newName = "Envigado";

        MunicipalityDTO previous = modelMapper.map(municipalityToCompare, MunicipalityDTO.class);
        MunicipalityDTO updated = modelMapper.map(municipalityToCompare, MunicipalityDTO.class);
        updated.setName(newName);

        given(municipalityService.update(Mockito.any(UUID.class), Mockito.any(MunicipalityDTO.class))).willReturn(updated);

        ResponseEntity<MunicipalityDTO> response = municipalityController.updateMunicipality(municipalityToCompare.getId(), updated);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        MunicipalityDTO municipalityDTO = response.getBody();

        Assertions.assertThat(municipalityDTO)
                .isNotNull()
                .isEqualTo(updated);
        Assertions.assertThat(municipalityDTO.getName()).isNotEqualTo(previous.getName());
    }

    @Test
    void MunicipalityController_DeleteMunicipality_ReturnsOk() {
        UUID randomId = UUID.randomUUID();

        ResponseEntity<Void> response = municipalityController.deleteMunicipality(randomId);
        verify(municipalityService).delete(randomId);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
