package web.mates.arriendatufinca.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.dto.RequestUserDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.helper.TestVariables;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.repository.UserRepository;
import web.mates.arriendatufinca.service.UserService;

import java.util.*;

@SpringBootTest(classes = ArriendatufincaApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManager entityManager;

    private final List<User> users = TestVariables.users;

    @BeforeEach
    void setup() {
        completelyDeleteUsers();
    }

    @Transactional
    void completelyDeleteUsers() {
        entityManager.joinTransaction();
        entityManager.createQuery("DELETE FROM User ").executeUpdate();
    }

    @Test
    void UserService_RegisterUser_ReturnsNewUserDTO() {
        int currentUsers = userService.getAllUsers().size();

        RequestUserDTO requestUserDTO = modelMapper.map(this.users.get(0), RequestUserDTO.class);
        UserDTO expectedResult = modelMapper.map(this.users.get(0), UserDTO.class);

        UserDTO savedUser = userService.newUser(requestUserDTO);
        expectedResult.setId(savedUser.getId());
        int afterUsers = userService.getAllUsers().size();

        Assertions.assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
        Assertions.assertThat(afterUsers).isEqualTo(currentUsers + 1);
    }

    @Test
    void UserService_GetAllUsers_ReturnsListUserDTO() {
        // Mocking users
        userRepository.saveAll(users);

        List<UserDTO> responseUsers = userService.getAllUsers();

        Assertions.assertThat(responseUsers.size()).isEqualTo(users.size());
        Assertions.assertThat(responseUsers).isNotNull();

        List<String> emails = new ArrayList<>();

        for (UserDTO u : responseUsers)
            emails.add(u.getEmail());

        for (User u : users)
            Assertions.assertThat(emails).contains(u.getEmail());

    }

    @Test
    void UserService_GetUserById_ReturnsUserDTO() {
        Iterable<User> currentUsers = userRepository.saveAll(users);
        User userToCompare = currentUsers.iterator().next();

        UserDTO dtoCompare = modelMapper.map(userToCompare, UserDTO.class);

        UserDTO foundUser = userService.getUserById(userToCompare.getId());

        Assertions.assertThat(foundUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(dtoCompare);
    }

    @Test
    void UserService_UpdateUser_ReturnsUpdatedUserDTO() {
        Iterable<User> currentUsers = userRepository.saveAll(users);
        User userToCompare = currentUsers.iterator().next();

        UserDTO updatedUser = modelMapper.map(userToCompare, UserDTO.class);
        updatedUser.setEmail("newmail@other.com");
        updatedUser.setName("Johnathan");

        UserDTO newUser = userService.updateUser(userToCompare.getId(), updatedUser);

        Assertions.assertThat(newUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }

    @Test
    void UserService_DeleteUser_ReturnsVoidAndDeletesUser() {
        User userToUse = users.get(0);
        userToUse.setProperties(new HashSet<>());
        User existingUser = userRepository.save(userToUse);

        userService.deleteUser(existingUser.getId());

        Optional<User> requestUser = userRepository.findById(existingUser.getId());
        Assertions.assertThat(requestUser.isPresent()).isFalse();
    }
}
