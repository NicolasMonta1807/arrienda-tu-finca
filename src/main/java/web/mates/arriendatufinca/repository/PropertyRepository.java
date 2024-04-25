package web.mates.arriendatufinca.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.model.User;

public interface PropertyRepository extends CrudRepository<Property, UUID> {
    Iterable<Property> findByOwner(User owner);
}
