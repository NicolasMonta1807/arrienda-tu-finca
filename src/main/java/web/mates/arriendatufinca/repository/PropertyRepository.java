package web.mates.arriendatufinca.repository;

import org.springframework.data.repository.CrudRepository;
import web.mates.arriendatufinca.model.municipality.Municipality;
import web.mates.arriendatufinca.model.property.Property;
import web.mates.arriendatufinca.model.user.User;

import java.util.UUID;

public interface PropertyRepository extends CrudRepository<Property, UUID> {
    Iterable<Property> findByMunicipalityOrName(Municipality municipality, String name);
    Iterable<Property> findByOwner(User owner);
}
