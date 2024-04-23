package web.mates.arriendatufinca.repository;

import org.springframework.data.repository.CrudRepository;
import web.mates.arriendatufinca.model.Department;

import java.util.UUID;

public interface DepartmentRepository extends CrudRepository<Department, UUID> {
}
