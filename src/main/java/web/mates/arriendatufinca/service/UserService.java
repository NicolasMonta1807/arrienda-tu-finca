package web.mates.arriendatufinca.service;

import jakarta.validation.Valid;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import web.mates.arriendatufinca.exceptions.DuplicateEmailException;
import web.mates.arriendatufinca.exceptions.UnauthorizedException;
import web.mates.arriendatufinca.exceptions.EntityNotFoundException;
import web.mates.arriendatufinca.model.user.User;
import web.mates.arriendatufinca.model.user.dto.SignUpDTO;
import web.mates.arriendatufinca.model.user.dto.SimpleUserDTO;
import web.mates.arriendatufinca.repository.UserRepository;
import web.mates.arriendatufinca.security.CustomUserDetailsService;
import web.mates.arriendatufinca.security.jwt.JWTFilter;
import web.mates.arriendatufinca.security.jwt.JWTUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtil jwtUtil;
    private final JWTFilter jwtFilter;

    UserService(UserRepository userRepository, ModelMapper modelMapper, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JWTUtil jwtUtil, JWTFilter jwtFilter) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtFilter = jwtFilter;
    }

    private void checkAuth(String emailToCheck) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getName().equalsIgnoreCase(emailToCheck))
            throw new UnauthorizedException("Not authorized");
    }

    public List<SimpleUserDTO> getAll() {
        if (!jwtFilter.isAdmin())
            throw new UnauthorizedException();

        Iterable<User> users = userRepository.findAll();
        List<SimpleUserDTO> userDTOS = new ArrayList<>();

        for (User user : users)
            userDTOS.add(modelMapper.map(user, SimpleUserDTO.class));

        return userDTOS;
    }

    public SimpleUserDTO getById(@NonNull UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent())
            return modelMapper.map(user, SimpleUserDTO.class);
        return null;
    }

    public SimpleUserDTO create(@NonNull SignUpDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        User userToSave = modelMapper.map(user, User.class);
        userToSave.setRole("user");
        User saved = userRepository.save(userToSave);
        return modelMapper.map(saved, SimpleUserDTO.class);
    }

    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (authentication.isAuthenticated())
            return jwtUtil.generateToken(
                    customUserDetailsService.getUserDetail().getId(),
                    customUserDetailsService.getUserDetail().getEmail(),
                    customUserDetailsService.getUserDetail().getRole());
        else
            throw new UnauthorizedException("Invalid Credentials");
    }

    public SimpleUserDTO update(@NonNull UUID id, @NonNull @Valid SimpleUserDTO user) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty())
            throw new EntityNotFoundException("User not found");

        User userToUpdate = foundUser.get();
        checkAuth(userToUpdate.getEmail());
        userToUpdate.setName(user.getName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());

        return modelMapper.map(userRepository.save(userToUpdate), SimpleUserDTO.class);
    }

    public void delete(@NonNull UUID id) {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty())
            return;

        checkAuth(foundUser.get().getEmail());
        userRepository.deleteById(id);
    }
}
