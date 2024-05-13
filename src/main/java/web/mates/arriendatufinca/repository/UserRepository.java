package web.mates.arriendatufinca.repository;

import org.springframework.data.repository.CrudRepository;
import web.mates.arriendatufinca.model.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
