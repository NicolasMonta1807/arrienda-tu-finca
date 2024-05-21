package web.mates.arriendatufinca.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mates.arriendatufinca.model.department.dto.SimpleDepartmentDTO;
import web.mates.arriendatufinca.service.DepartmentService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/department")
@Tag(name = "Departments", description = "Departments management")
@ApiResponse(
        responseCode = "403",
        description = "No Authorization Token",
        content = @Content
)
public class DepartmentController {
    private final DepartmentService departmentService;

    DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(
            summary = "Get all departments",
            description = "Get a list of details for each department"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleDepartmentDTO.class))
            )
    })
    @GetMapping()
    public ResponseEntity<List<SimpleDepartmentDTO>> getAll() {
        return new ResponseEntity<>(departmentService.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get department by ID",
            description = "Get a single department knowing its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleDepartmentDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Department with given ID does not exist",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SimpleDepartmentDTO> getById(
            @Parameter(
                    name = "id",
                    description = "ID of department to be retrieved",
                    required = true
            )
            @NonNull @PathVariable UUID id
    ) {
        return new ResponseEntity<>(departmentService.getById(id), HttpStatus.OK);
    }

    @Tag(name = "Admin-only", description = "Operations valid only for token's from admins")
    @Operation(
            summary = "New department",
            description = "Creates a new department"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleDepartmentDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more fields do not comply with current constraints",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Given token does not belong to an admin",
                    content = @Content
            )
    })
    @PostMapping()
    public ResponseEntity<SimpleDepartmentDTO> create(
            @Parameter(
                    name = "department",
                    description = "New department information",
                    schema = @Schema(implementation = SimpleDepartmentDTO.class),
                    required = true
            )
            @NonNull @Valid @RequestBody SimpleDepartmentDTO department
    ) {
        return new ResponseEntity<>(departmentService.create(department), HttpStatus.CREATED);
    }

    @Tag(name = "Admin-only", description = "Operations valid only for token's from admins")
    @Operation(
            summary = "Update department",
            description = "Updates a department information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated department information",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleDepartmentDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more fields do not comply with current constraints",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Given token does not belong to an admin",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SimpleDepartmentDTO> update(
            @Parameter(
                    name = "id",
                    description = "ID of department to be updated",
                    schema = @Schema(implementation = UUID.class),
                    required = true
            )
            @NonNull @PathVariable UUID id,
            @Parameter(
                    name = "department",
                    description = "Updated department information",
                    schema = @Schema(implementation = SimpleDepartmentDTO.class),
                    required = true
            )
            @NonNull @Valid @RequestBody SimpleDepartmentDTO department) {
        return new ResponseEntity<>(departmentService.update(id, department), HttpStatus.OK);
    }

    @Tag(name = "Admin-only", description = "Operations valid only for token's from admins")
    @Operation(
            summary = "Delete department",
            description = "Mark a department as deleted"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Updated department information",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleDepartmentDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Given token does not belong to an admin",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    name = "id",
                    description = "ID of department to be updated",
                    schema = @Schema(implementation = UUID.class),
                    required = true
            )
            @NonNull @PathVariable UUID id
    ) {
        departmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
