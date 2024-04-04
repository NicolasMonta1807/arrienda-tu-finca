package web.mates.arriendatufinca.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import web.mates.arriendatufinca.model.Municipality;
import web.mates.arriendatufinca.repository.MunicipalityRepository;
import web.mates.arriendatufinca.service.MunicipalityService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = ArriendatufincaApplication.class)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class MunicipalityServiceTests {

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @Autowired
    private MunicipalityService municipalityService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManager entityManager;

    private final List<Municipality> municipalities = TestVariables.municipalities;

    @BeforeEach
    void setup() {
        completelyDeleteMunicipalities();
    }

    @Transactional
    void completelyDeleteMunicipalities() {
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM Municipality where deleted = false ").executeUpdate();
    }

    @Test
    void MunicipalityService_CreateMunicipality_ReturnsNewMunicipalityDTO() {
        int currentMunies = municipalityService.getAll().size();

        MunicipalityDTO municipalityDTO = modelMapper.map(municipalities.get(0), MunicipalityDTO.class);

        MunicipalityDTO savedMunie = municipalityService.create(municipalityDTO);
        municipalityDTO.setId(savedMunie.getId());

        int afterMunies = municipalityService.getAll().size();

        Assertions.assertThat(savedMunie)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(municipalityDTO);
        Assertions.assertThat(afterMunies).isEqualTo(currentMunies + 1);
    }

    @Test
    void MunicipalityService_GetMunicipalityById_ReturnsMunicipalityDTO() {
        Iterable<Municipality> currentMunies = municipalityRepository.saveAll(municipalities);
        Municipality munieToCompare = currentMunies.iterator().next();

        MunicipalityDTO dtoCompare = modelMapper.map(munieToCompare, MunicipalityDTO.class);

        MunicipalityDTO foundMunie = municipalityService.getById(munieToCompare.getId());

        Assertions.assertThat(foundMunie)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(dtoCompare);
    }

    @Test
    void MunicipalityService_UpdateMunicipality_ReturnsMunicipalityDTO() {
        Iterable<Municipality> currentMunies = municipalityRepository.saveAll(municipalities);
        Municipality munieToCompare = currentMunies.iterator().next();

        MunicipalityDTO updated = modelMapper.map(munieToCompare, MunicipalityDTO.class);
        updated.setName("Envigado");

        MunicipalityDTO newMunie = municipalityService.update(munieToCompare.getId(), updated);

        Assertions.assertThat(newMunie)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    void MunicipalityService_DeleteMunicipality_ReturnsVoidAndDeletesUser() {
        Municipality munieToDelete = municipalities.get(0);
        munieToDelete.setProperties(new HashSet<>());

        Municipality exisingMunie = municipalityRepository.save(munieToDelete);

        municipalityService.delete(exisingMunie.getId());

        Optional<Municipality> requestMunie = municipalityRepository.findById(exisingMunie.getId());
        Assertions.assertThat(requestMunie).isNotPresent();
    }
}
