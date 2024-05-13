package web.mates.arriendatufinca.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import web.mates.arriendatufinca.model.property.dto.NewPropertyDTO;
import web.mates.arriendatufinca.model.property.dto.SimplePropertyDTO;
import web.mates.arriendatufinca.service.PropertyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/property")
@Tag(name = "Property", description = "Properties' information")
@ApiResponse(
        responseCode = "403",
        description = "No Authorization Token",
        content = @Content
)
public class PropertyController {
    private final PropertyService propertyService;

    PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Operation(
            summary = "All available properties",
            description = "Get a list of the details for every existing property"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimplePropertyDTO.class))
            )
    })
    @GetMapping()
    public ResponseEntity<List<SimplePropertyDTO>> getAll() {
        return new ResponseEntity<>(propertyService.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Property by id",
            description = "Get a single Property knowing its ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SimplePropertyDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Property with given ID does not exist",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SimplePropertyDTO> getById(
            @Parameter(
                    name = "id",
                    description = "ID of user to be retrieved",
                    required = true
            )
            @NonNull @PathVariable UUID id
    ) {
        return new ResponseEntity<>(propertyService.getById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Get properties from owner",
            description = "Get the properties whose owner is current authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimplePropertyDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User does not have any properties yet",
                    content = @Content
            )
    })
    @GetMapping("/me")
    public ResponseEntity<List<SimplePropertyDTO>> getFromOwner() {
        return new ResponseEntity<>(propertyService.getFromOwner(), HttpStatus.OK);
    }

    @Operation(
            summary = "Property finder",
            description = "Retrieves all properties that match either a name or a given municipality"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimplePropertyDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No property was found with given parameters",
                    content = @Content
            )
    })
    @GetMapping("/find")
    public ResponseEntity<List<SimplePropertyDTO>> finder(
            @Parameter(
                    name = "municipality",
                    description = "ID of desired municipality's properties",
                    schema = @Schema(implementation = UUID.class)
            )
            @RequestParam(required = false) UUID municipality,
            @Parameter(
                    name = "name",
                    description = "Name of the desired property",
                    schema = @Schema(implementation = String.class)
            )
            @RequestParam(required = false) String name
    ) {
        return new ResponseEntity<>(propertyService.finder(municipality, name), HttpStatus.OK);
    }

    @Operation(
            summary = "New property",
            description = "Creates a property for current authenticated user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Created property information. Property owner is set to current authenticated user given in Authorization Token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimplePropertyDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more fields do not comply with current constraints",
                    content = @Content
            )
    })
    @PostMapping()
    public ResponseEntity<SimplePropertyDTO> create(
            @Parameter(
                    name = "property",
                    description = "New property information",
                    required = true,
                    schema = @Schema(implementation = NewPropertyDTO.class)
            )
            @NonNull @Valid @RequestBody NewPropertyDTO property
    ) {
        return new ResponseEntity<>(propertyService.create(property), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update property",
            description = "Updates given ID property information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimplePropertyDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Given token does not belong to property's owner",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SimplePropertyDTO> update(
            @Parameter(
                    name = "id",
                    description = "ID of property to be updated",
                    schema = @Schema(implementation = UUID.class),
                    required = true
            )
            @NonNull @PathVariable UUID id,
            @Parameter(
                    name = "property",
                    in = ParameterIn.QUERY,
                    description = "Updated property information",
                    schema = @Schema(implementation = NewPropertyDTO.class),
                    required = true
            )
            @NonNull @Valid @RequestBody NewPropertyDTO property
    ) {
        return new ResponseEntity<>(propertyService.update(id, property), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete property",
            description = "Mark the given property as deleted"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Operation was completed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Given token does not belong to deleting property's owner",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Valid> delete(
            @Parameter(
                    name = "id",
                    description = "ID of property to be deleted",
                    schema = @Schema(implementation = UUID.class),
                    required = true
            )
            @NonNull @PathVariable UUID id
    ) {
        propertyService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
