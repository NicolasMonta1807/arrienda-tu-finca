package web.mates.arriendatufinca.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import web.mates.arriendatufinca.model.Property;

public interface PropertyRepository extends CrudRepository<Property, UUID> {

}
