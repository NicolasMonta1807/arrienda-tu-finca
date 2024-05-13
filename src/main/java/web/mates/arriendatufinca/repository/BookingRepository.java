package web.mates.arriendatufinca.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import web.mates.arriendatufinca.model.booking.Booking;

import java.util.UUID;

public interface BookingRepository extends CrudRepository<Booking, UUID> {
    @Query("FROM Booking b WHERE b.lessee.id = :userId")
    Iterable<Booking> findByUserAsLessee(@Param("userId") UUID userId);

    @Query("FROM Booking b WHERE b.property.owner.id = :userId")
    Iterable<Booking> findByUserAsLessor(@Param("userId") UUID userId);
}
