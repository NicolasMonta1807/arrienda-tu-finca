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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.mates.arriendatufinca.model.property.dto.LoginDTO;
import web.mates.arriendatufinca.model.user.dto.SignUpDTO;
import web.mates.arriendatufinca.model.user.dto.SimpleUserDTO;
import web.mates.arriendatufinca.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "JWT and Security related operations")
public class AuthController {
    private final UserService userService;

    AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Sign Up",
            description = "Create a new user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SimpleUserDTO.class))}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more fields are not properly formatted or do not comply with current constraints. Response returns a description of which fields must be fixed",
                    content = @Content
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<SimpleUserDTO> signUp(
            @Parameter(
                    description = "User object to register",
                    required = true,
                    schema = @Schema(implementation = SignUpDTO.class)
            )
            @NonNull @RequestBody @Valid SignUpDTO user
    ) {
        return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login",
            description = "Authenticate and retrieve JWT Token for user credentials"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid Credentials",
                    content = @Content

            )
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginDTO credentials) {
        String email = credentials.getEmail();
        String password = credentials.getPassword();
        String token = userService.login(email, password);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
