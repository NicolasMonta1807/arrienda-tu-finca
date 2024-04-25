package web.mates.arriendatufinca.repository;

import org.springframework.data.repository.CrudRepository;
import web.mates.arriendatufinca.model.Booking;
import web.mates.arriendatufinca.model.Property;
import web.mates.arriendatufinca.model.User;

import java.util.UUID;

public interface BookingRepository extends CrudRepository<Booking, UUID> {
    Iterable<Booking> findByLessee(User lessee);

    Iterable<Booking> findByProperty(Property property);
}
