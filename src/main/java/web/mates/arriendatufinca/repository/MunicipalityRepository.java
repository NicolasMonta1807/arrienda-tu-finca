package web.mates.arriendatufinca.repository;

import org.springframework.data.repository.CrudRepository;
import web.mates.arriendatufinca.model.municipality.Municipality;

import java.util.UUID;

public interface MunicipalityRepository extends CrudRepository<Municipality, UUID> {
}
