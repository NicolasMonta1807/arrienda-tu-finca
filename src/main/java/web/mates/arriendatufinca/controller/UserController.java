package web.mates.arriendatufinca.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.NonNull;
import web.mates.arriendatufinca.dto.RequestUserDTO;
import web.mates.arriendatufinca.dto.SignInDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.service.UserService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@NonNull @PathVariable UUID id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<UserDTO> newUser(@NonNull @RequestBody @Valid RequestUserDTO user) {
        return new ResponseEntity<>(userService.newUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@NonNull @RequestBody SignInDTO user) {
        return new ResponseEntity<>(userService.login(user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@NonNull @PathVariable @Valid UUID id, @NonNull @RequestBody UserDTO newUser) {
        return new ResponseEntity<>(userService.updateUser(id, newUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@NonNull @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
