package web.mates.arriendatufinca.repository;

import org.springframework.data.repository.CrudRepository;
import web.mates.arriendatufinca.model.department.Department;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends CrudRepository<Department, UUID> {
    Optional<Department> findByName(String name);
}
