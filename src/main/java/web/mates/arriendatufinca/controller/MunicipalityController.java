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
import web.mates.arriendatufinca.model.municipality.SimpleMunicipalityDTO;
import web.mates.arriendatufinca.service.MunicipalityService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/municipality")
@Tag(name = "Municipalities", description = "Municipalities management")
@ApiResponse(
        responseCode = "403",
        description = "No Authorization Token",
        content = @Content
)
public class MunicipalityController {
    private final MunicipalityService municipalityService;

    MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @Operation(
            summary = "Get all municipalities",
            description = "Get a list of details for each municipality"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleMunicipalityDTO.class))
            )
    })
    @GetMapping()
    public ResponseEntity<List<SimpleMunicipalityDTO>> getAll() {
        return new ResponseEntity<>(municipalityService.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Get municipality by ID",
            description = "Get a single municipality knowing its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleMunicipalityDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Municipality with given ID does not exist",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SimpleMunicipalityDTO> getById(
            @Parameter(
                    name = "id",
                    description = "ID of municipality to be retrieved",
                    required = true
            )
            @NonNull @PathVariable UUID id
    ) {
        return new ResponseEntity<>(municipalityService.getById(id), HttpStatus.OK);
    }

    @Tag(name = "Admin-only", description = "Operations valid only for token's from admins")
    @Operation(
            summary = "New municipality",
            description = "Creates a new municipality"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleMunicipalityDTO.class))
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
    public ResponseEntity<SimpleMunicipalityDTO> create(
            @Parameter(
                    name = "municipality",
                    description = "New municipality information",
                    schema = @Schema(implementation = SimpleMunicipalityDTO.class),
                    required = true
            )
            @NonNull @Valid @RequestBody SimpleMunicipalityDTO municipality
    ) {
        return new ResponseEntity<>(municipalityService.create(municipality), HttpStatus.CREATED);
    }

    @Tag(name = "Admin-only", description = "Operations valid only for token's from admins")
    @Operation(
            summary = "Update municipality",
            description = "Updates a municipality information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated municipality information",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleMunicipalityDTO.class))
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
    public ResponseEntity<SimpleMunicipalityDTO> update(
            @Parameter(
                    name = "id",
                    description = "ID of municipality to be updated",
                    schema = @Schema(implementation = UUID.class),
                    required = true
            )
            @NonNull @PathVariable UUID id,
            @Parameter(
                    name = "department",
                    description = "Updated municipality information",
                    schema = @Schema(implementation = SimpleMunicipalityDTO.class),
                    required = true
            )
            @NonNull @Valid @RequestBody SimpleMunicipalityDTO municipality
    ) {
        return new ResponseEntity<>(municipalityService.update(id, municipality), HttpStatus.OK);
    }

    @Tag(name = "Admin-only", description = "Operations valid only for token's from admins")
    @Operation(
            summary = "Delete municipality",
            description = "Mark a municipality as deleted"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Updated municipality information",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleMunicipalityDTO.class))
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
                    description = "ID of municipality to be updated",
                    schema = @Schema(implementation = UUID.class),
                    required = true
            )
            @NonNull @PathVariable UUID id
    ) {
        municipalityService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
