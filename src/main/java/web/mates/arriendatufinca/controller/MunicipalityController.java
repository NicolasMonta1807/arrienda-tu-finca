package web.mates.arriendatufinca.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.mates.arriendatufinca.dto.MunicipalityDTO;
import web.mates.arriendatufinca.service.MunicipalityService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/municipality")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<MunicipalityDTO>> getAllMunicipalities() {
        return new ResponseEntity<>(municipalityService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityDTO> getMunicipalityById(@NonNull @PathVariable UUID id) {
        MunicipalityDTO municipality = municipalityService.getById(id);
        if (municipality == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "municipality not found");
        }
        return new ResponseEntity<>(municipality, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<MunicipalityDTO> createMunicipality(@NonNull @Valid @RequestBody MunicipalityDTO municipalityDTO) {
        return new ResponseEntity<>(municipalityService.create(municipalityDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MunicipalityDTO> updateMunicipality(@NonNull @Valid @PathVariable UUID id, @NonNull @RequestBody MunicipalityDTO municipalityDTO) {
        MunicipalityDTO updatedMunicipality = municipalityService.update(id, municipalityDTO);
        if (updatedMunicipality == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "municipality not found");
        }
        return new ResponseEntity<>(updatedMunicipality, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMunicipality(@NonNull @PathVariable UUID id) {
        municipalityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
