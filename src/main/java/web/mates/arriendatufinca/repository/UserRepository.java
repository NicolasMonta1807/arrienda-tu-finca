package web.mates.arriendatufinca.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import web.mates.arriendatufinca.model.User;

public interface UserRepository extends CrudRepository<User, UUID> {

}
