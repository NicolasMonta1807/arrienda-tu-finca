package web.mates.arriendatufinca;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import web.mates.arriendatufinca.controller.UserController;
import web.mates.arriendatufinca.dto.RequestUserDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArriendatufincaApplication.class
)
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class ArriendatufincaApplicationTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final List<User> users = new ArrayList<>();

    @BeforeEach
    public void setup() {
        this.users.add(User.builder()

                .name("John")
                .lastName("Doe")
                .email("mail@domain.com")
                .phoneNumber("3003898475")
                .password("pass1234")
                .build());

        this.users.add(User.builder()
                .name("Jane")
                .lastName("Doe")
                .email("correo@mail.com")
                .phoneNumber("3123235206")
                .password("contra1234")
                .build());
    }

    @Test
    void UserService_RegisterUser_ReturnsUserDTO()  {
        User userToCompare = this.users.get(0);

        RequestUserDTO userDTO = RequestUserDTO.builder()
                .name(userToCompare.getName())
                .lastName(userToCompare.getLastName())
                .email(userToCompare.getEmail())
                .phoneNumber(userToCompare.getPhoneNumber())
                .password(userToCompare.getPassword())
                .build();

        UUID generatedID = UUID.randomUUID();

        given(userService.newUser(Mockito.any(RequestUserDTO.class)))
                .willReturn(UserDTO.builder()
                        .id(generatedID)
                        .name(userToCompare.getName())
                        .lastName(userToCompare.getLastName())
                        .email(userToCompare.getEmail())
                        .phoneNumber(userToCompare.getPhoneNumber())
                        .build());

        ResponseEntity<UserDTO> response = userController.newUser(userDTO);
        UserDTO savedUser = response.getBody();

        UserDTO expectedResult = UserDTO.builder()
                .id(generatedID)
                .name(this.users.get(0).getName())
                .lastName(this.users.get(0).getLastName())
                .email(this.users.get(0).getEmail())
                .phoneNumber(this.users.get(0).getPhoneNumber())
                .build();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);


        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser)
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }
}
