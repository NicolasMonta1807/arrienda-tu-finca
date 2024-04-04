package web.mates.arriendatufinca.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.controller.UserController;
import web.mates.arriendatufinca.dto.RequestUserDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.helper.TestVariables;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArriendatufincaApplication.class
)
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final List<User> users = TestVariables.users;

    @Test
    void UserController_RegisterUser_ReturnsUserDTO() {
        User userToCompare = this.users.get(0);

        RequestUserDTO userDTO = RequestUserDTO.builder()
                .name(userToCompare.getName())
                .lastName(userToCompare.getLastName())
                .email(userToCompare.getEmail())
                .phoneNumber(userToCompare.getPhoneNumber())
                .password(userToCompare.getPassword())
                .build();

        UserDTO expectedResult = UserDTO.builder()
                .id(userToCompare.getId())
                .name(userToCompare.getName())
                .lastName(userToCompare.getLastName())
                .email(userToCompare.getEmail())
                .phoneNumber(userToCompare.getPhoneNumber())
                .build();

        given(userService.newUser(Mockito.any(RequestUserDTO.class))).willReturn(expectedResult);

        ResponseEntity<UserDTO> response = userController.newUser(userDTO);
        UserDTO savedUser = response.getBody();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);


        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser)
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }

    @Test
    void UserController_GetAllUsers_ReturnsListUserDTO() {
        List<UserDTO> usersToCompare = new ArrayList<>();
        for (User u : users) {
            usersToCompare.add(UserDTO.builder()
                    .id(u.getId())
                    .name(u.getName())
                    .lastName(u.getLastName())
                    .email(u.getEmail())
                    .phoneNumber(u.getPhoneNumber())
                    .build());
        }

        given(userService.getAllUsers()).willReturn(usersToCompare);

        ResponseEntity<List<UserDTO>> response = userController.getUsers();
        List<UserDTO> users = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        for (UserDTO userDTO : usersToCompare) {
            Assertions.assertThat(users).contains(userDTO);
        }
    }

    @Test
    void UserController_GetSingleUserById_ReturnsUserDTO() {
        User userToCompare = this.users.get(0);

        UserDTO expectedResult = UserDTO.builder()
                .name(userToCompare.getName())
                .lastName(userToCompare.getLastName())
                .email(userToCompare.getEmail())
                .phoneNumber(userToCompare.getPhoneNumber())
                .build();

        given(userService.getUserById(Mockito.any(UUID.class))).willReturn(expectedResult);

        ResponseEntity<UserDTO> response = userController.getUserById(userToCompare.getId());
        UserDTO user = response.getBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    void UserController_GetUserWithNoExisingId_ReturnsNotFound() {
        ResponseEntity<UserDTO> response = userController.getUserById(UUID.randomUUID());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void UserController_UpdateUser_ReturnsUpdatedUserDTO() {
        User userToCompare = this.users.get(0);

        String newName = "Jane";

        UserDTO previousUser = UserDTO.builder()
                .name(userToCompare.getName())
                .lastName(userToCompare.getLastName())
                .email(userToCompare.getEmail())
                .phoneNumber(userToCompare.getPhoneNumber())
                .build();

        UserDTO updatedUser = UserDTO.builder()
                .name(newName)
                .lastName(userToCompare.getLastName())
                .email(userToCompare.getEmail())
                .phoneNumber(userToCompare.getPhoneNumber())
                .build();

        given(userService.updateUser(Mockito.any(UUID.class), Mockito.any(UserDTO.class))).willReturn(updatedUser);

        ResponseEntity<UserDTO> response = userController.updateUser(userToCompare.getId(), updatedUser);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserDTO userResponse = response.getBody();
        Assertions.assertThat(userResponse)
                .isNotNull()
                .isEqualTo(updatedUser)
                .isNotEqualTo(previousUser);
    }

    @Test
    void UserController_DeleteUser_ReturnsOk() {
        UUID randomId = UUID.randomUUID();

        ResponseEntity<Void> response = userController.deleteUser(randomId);
        verify(userService).deleteUser(randomId);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
