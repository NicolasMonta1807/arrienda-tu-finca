package web.mates.arriendatufinca.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.model.User;

public interface PropertyRepository extends CrudRepository<Property, UUID> {
    Property findByOwner(User owner);
}
