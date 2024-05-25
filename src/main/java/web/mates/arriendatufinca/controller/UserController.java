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
import web.mates.arriendatufinca.model.user.dto.SimpleUserDTO;
import web.mates.arriendatufinca.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "Users' information")
@ApiResponse(
        responseCode = "403",
        description = "No Authorization Token",
        content = @Content
)
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @Tag(name = "Admin-only", description = "Operations valid only for token's from admins")
    @Operation(
            summary = "All users",
            description = "Get a list of all existing and active users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SimpleUserDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. Authorization Token is not admin",
                    content = @Content
            )
    })
    @GetMapping()
    public ResponseEntity<List<SimpleUserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "User by id",
            description = "Get a single user knowing their ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SimpleUserDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User with given ID does not exist",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SimpleUserDTO> getUserById(
            @Parameter(
                    name = "id",
                    description = "ID of user to be retrieved",
                    required = true)
            @NonNull @PathVariable UUID id
    ) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Update user",
            description = "Update given user ID information"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimpleUserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more fields are not properly formatted or do not comply with current constraints. Response returns a description of which fields must be fixed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Given token does not belong to updating user",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SimpleUserDTO> updateUser(
            @Parameter(
                    description = "ID of user to be updated",
                    required = true,
                    schema = @Schema(implementation = UUID.class)
            )
            @PathVariable @NonNull UUID id,
            @Parameter(
                    description = "New user information",
                    required = true,
                    schema = @Schema(implementation = SimpleUserDTO.class)
            )
            @RequestBody @Valid SimpleUserDTO user) {
        return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete user",
            description = "Mark the given user ID as deleted"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Operation was completed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Given token does not belong to deleting user",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(
            @Parameter(
                    description = "ID of the user to be deleted",
                    required = true,
                    schema = @Schema(implementation = UUID.class)
            )
            @PathVariable @NonNull UUID id
    ) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
