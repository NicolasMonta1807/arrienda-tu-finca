package web.mates.arriendatufinca.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import web.mates.arriendatufinca.dto.PropertyDTO;
import web.mates.arriendatufinca.service.PropertyService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  PropertyService propertyService;

  @GetMapping(value = { "", "/" })
  public List<PropertyDTO> getProperties() {
    return propertyService.getAllProperties();
  }

  @GetMapping("/{id}")
  public PropertyDTO getPropertyById(@NonNull @PathVariable UUID id) {
    return propertyService.getPropertyById(id);
  }

  @PostMapping(value = { "", "/" })
  public PropertyDTO newProperty(@NonNull @RequestBody PropertyDTO property) {
    return propertyService.newProperty(property);
  }

  @PutMapping("/{id}")
  public PropertyDTO updateProperty(@NonNull @PathVariable UUID id, @NonNull @RequestBody PropertyDTO newProperty) {
    return propertyService.updateProperty(id, newProperty);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteProperty(@NonNull @PathVariable UUID id) {
    if (propertyService.deleteProperty(id)) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.notFound().build();
  }

}
