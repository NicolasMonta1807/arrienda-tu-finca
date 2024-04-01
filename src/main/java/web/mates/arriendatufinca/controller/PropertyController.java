package web.mates.arriendatufinca.controller;

import jakarta.validation.Valid;
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
    public List<PropertyDTO> getProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/{id}")
    public PropertyDTO getPropertyById(@NonNull @PathVariable UUID id) {
        return propertyService.getPropertyById(id);
    }

    @PostMapping(value = {"", "/"})
    public PropertyDTO newProperty(@NonNull @Valid @RequestBody PropertyDTO property) {
        return propertyService.newProperty(property);
    }

    @PutMapping("/{id}")
    public PropertyDTO updateProperty(@NonNull @Valid @PathVariable UUID id, @NonNull @RequestBody PropertyDTO newProperty) {
        return propertyService.updateProperty(id, newProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@NonNull @PathVariable UUID id) {
        if (Boolean.TRUE.equals(propertyService.deleteProperty(id))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
