package web.mates.arriendatufinca.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private UserRepository userRepository;

  @GetMapping(value = { "", "/" })
  public Iterable<User> getUsers() {
    return userRepository.findAll();
  }

  @GetMapping("/{id}")
  public Optional<User> getUserById(@NonNull @PathVariable UUID id) {
    return userRepository.findById(id);
  }

  @PostMapping(value = { "", "/" })
  public User newUser(@NonNull @RequestBody User user) {
    userRepository.save(user);
    return user;
  }

  @PutMapping("/{id}")
  public User updateUser(@NonNull @PathVariable UUID id, @NonNull @RequestBody User newUser) {
    return userRepository.findById(id).map(user -> {
      return userRepository.save(newUser);
    }).orElseGet(() -> {
      return null;
    });
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@NonNull @PathVariable UUID id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      userRepository.deleteById(id);
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
