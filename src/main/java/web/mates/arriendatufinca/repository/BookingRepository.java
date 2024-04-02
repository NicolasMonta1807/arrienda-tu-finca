package web.mates.arriendatufinca.repository;

import org.springframework.data.repository.CrudRepository;
import web.mates.arriendatufinca.model.Booking;

import java.util.UUID;

public interface BookingRepository extends CrudRepository<Booking, UUID> {
}
