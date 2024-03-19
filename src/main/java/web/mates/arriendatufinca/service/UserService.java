package web.mates.arriendatufinca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import web.mates.arriendatufinca.dto.RequestUserDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  UserService(UserRepository userRepository, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
  }

  public List<UserDTO> getAllUsers() {
    Iterable<User> users = userRepository.findAll();
    List<UserDTO> usersDTO = new ArrayList<>();

    for (User user : users) {
      usersDTO.add(modelMapper.map(user, UserDTO.class));
    }
    return usersDTO;
  }

  public UserDTO getUserById(@NonNull UUID id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent())
      return modelMapper.map(user.get(), UserDTO.class);
    return null;
  }

  public UserDTO newUser(@NonNull RequestUserDTO user) {
    User newUser = modelMapper.map(user, User.class);

    if (newUser == null) {
      return null;
    }

    userRepository.save(newUser);
    return getUserById(newUser.getId());
  }

  public UserDTO updateUser(@NonNull UUID id, @NonNull UserDTO newUser) {
    User user = userRepository.findById(id).get();
    user.setName(newUser.getName());
    user.setEmail(newUser.getEmail());
    userRepository.save(user);
    return newUser;
  }

  public Boolean deleteUser(@NonNull UUID id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
