package web.mates.arriendatufinca.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.repository.PropertyRepository;

import java.util.Optional;
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
  PropertyRepository propertyRepository;

  @GetMapping(value = { "", "/" })
  public Iterable<Property> getProperties() {
    return propertyRepository.findAll();
  }

  @GetMapping("/{id}")
  public Optional<Property> getPropertyById(@NonNull @PathVariable UUID id) {
    return propertyRepository.findById(id);
  }

  @PostMapping(value = { "", "/" })
  public Property newProperty(@NonNull @RequestBody Property property) {
    return propertyRepository.save(property);
  }

  @PutMapping("/{id}")
  public Property updateProperty(@NonNull @PathVariable UUID id, @NonNull @RequestBody Property newProperty) {
    return propertyRepository.findById(id).map(property -> {
      return propertyRepository.save(newProperty);
    }).orElseGet(() -> {
      return null;
    });
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteProperty(@NonNull @PathVariable UUID id) {
    Optional<Property> property = propertyRepository.findById(id);
    if (property.isPresent()) {
      propertyRepository.deleteById(id);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
