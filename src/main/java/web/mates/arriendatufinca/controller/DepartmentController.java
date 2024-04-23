package web.mates.arriendatufinca.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.mates.arriendatufinca.dto.DepartmentDTO;
import web.mates.arriendatufinca.service.DepartmentService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final ServerProperties serverProperties;

    DepartmentController(DepartmentService departmentService, ServerProperties serverProperties) {
        this.departmentService = departmentService;
        this.serverProperties = serverProperties;
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return new ResponseEntity<>(departmentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@NonNull @PathVariable UUID id) {
        DepartmentDTO department = departmentService.getById(id);
        if (department == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "department not found");
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<DepartmentDTO> createDepartment(@NonNull @Valid @RequestBody DepartmentDTO department) {
        return new ResponseEntity<>(departmentService.create(department), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@NonNull @PathVariable UUID id, @NonNull @Valid @RequestBody DepartmentDTO department) {
        DepartmentDTO updatedDepartment = departmentService.update(id, department);
        if (updatedDepartment == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "department not found");
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@NonNull @PathVariable UUID id) {
        departmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
