package web.mates.arriendatufinca.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import web.mates.arriendatufinca.dto.PropertyDTO;
import web.mates.arriendatufinca.service.PropertyService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/property")
public class PropertyController {

    private final PropertyService propertyService;

    PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<PropertyDTO>> getProperties() {
        return new ResponseEntity<>(propertyService.getAllProperties(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyDTO> getPropertyById(@NonNull @PathVariable UUID id) {
        return new ResponseEntity<>(propertyService.getPropertyById(id), HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<PropertyDTO> newProperty(@NonNull @Valid @RequestBody PropertyDTO property) {
        return new ResponseEntity<>(propertyService.newProperty(property), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(@NonNull @Valid @PathVariable UUID id, @NonNull @RequestBody PropertyDTO newProperty) {
        return new ResponseEntity<>(propertyService.updateProperty(id, newProperty), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@NonNull @PathVariable UUID id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }

}
