package web.mates.arriendatufinca.repository;

import org.springframework.data.repository.CrudRepository;
import web.mates.arriendatufinca.model.Review;

import java.util.UUID;

public interface ReviewRepository extends CrudRepository<Review, UUID> {
}
