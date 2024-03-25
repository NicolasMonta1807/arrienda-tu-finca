package web.mates.arriendatufinca.controller;

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
@RequestMapping("/municipality")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @GetMapping(value = {"", "/"})
    public List<MunicipalityDTO> getAllMunicipalities() {
        return municipalityService.getAll();
    }

    @GetMapping("/{id}")
    public MunicipalityDTO getMunicipalityById(@NonNull @PathVariable UUID id) {
        MunicipalityDTO municipality = municipalityService.getById(id);
        if (municipality != null) {
            return municipality;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "municipality not found");
    }

    @PostMapping(value = {"", "/"})
    public MunicipalityDTO createMunicipality(@NonNull @RequestBody MunicipalityDTO municipalityDTO) {
        return municipalityService.create(municipalityDTO);
    }

    @PutMapping("/{id}")
    public MunicipalityDTO updateMunicipality(@NonNull @PathVariable UUID id, @NonNull @RequestBody MunicipalityDTO municipalityDTO) {
        return municipalityService.update(id, municipalityDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMunicipality(@NonNull @PathVariable UUID id) {
        municipalityService.delete(id);
        return ResponseEntity.ok("");
    }

}
