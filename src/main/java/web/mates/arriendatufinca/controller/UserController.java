package web.mates.arriendatufinca.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import web.mates.arriendatufinca.dto.RequestUserDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.service.UserService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"", "/"})
    public List<UserDTO> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@NonNull @PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PostMapping(value = {"", "/"})
    public UserDTO newUser(@NonNull @RequestBody RequestUserDTO user) {
        return userService.newUser(user);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@NonNull @PathVariable UUID id, @NonNull @RequestBody UserDTO newUser) {
        return userService.updateUser(id, newUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@NonNull @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
