package web.mates.arriendatufinca.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import web.mates.arriendatufinca.model.review.Review;

import java.util.UUID;

public interface ReviewRepository extends CrudRepository<Review, UUID> {
    @Query("FROM Review r WHERE r.author.id = :userId")
    Iterable<Review> findByUserAsAuthor(@Param("userId") UUID userId);

    @Query("FROM Review r WHERE r.rated.id = :userId")
    Iterable<Review> findByUserAsRated(@Param("userId") UUID userId);
}
