package web.mates.arriendatufinca.service;

import java.util.*;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import web.mates.arriendatufinca.dto.RequestUserDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.exceptions.DuplicateEmailException;
import web.mates.arriendatufinca.model.Property;
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
        else
            throw new EntityNotFoundException("User not found");
    }

    public UserDTO newUser(@NonNull RequestUserDTO user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        User newUser = modelMapper.map(user, User.class);

        if (newUser == null) {
            return null;
        }

        User savedUser = userRepository.save(newUser);
        return getUserById(savedUser.getId());
    }

    public UserDTO updateUser(@NonNull UUID id, @NonNull UserDTO newUser) {
        Optional<User> requestedUser = userRepository.findById(id);
        if (requestedUser.isPresent()) {
            User user = requestedUser.get();
            user.setName(newUser.getName());
            user.setEmail(newUser.getEmail());
            user.setPhoneNumber(newUser.getPhoneNumber());
            userRepository.save(user);
            return newUser;
        } else
            throw new EntityNotFoundException("User not found");
    }

    public void removeProperty(@NonNull UUID userId, @NonNull Property property) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Set<Property> properties = user.get().getProperties();
            properties.remove(property);
            user.get().setProperties(properties);
            userRepository.save(user.get());
        }
    }

    public void deleteUser(@NonNull UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Set<Property> properties = user.get().getProperties();
            properties.forEach(p -> p.setDeleted(true));
            user.get().setProperties(properties);
            userRepository.save(user.get());
            userRepository.deleteById(id);
        }
    }
}
