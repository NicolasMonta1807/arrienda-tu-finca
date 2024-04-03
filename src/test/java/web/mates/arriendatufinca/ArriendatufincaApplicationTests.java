package web.mates.arriendatufinca;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.ui.ModelMap;
import web.mates.arriendatufinca.dto.RequestUserDTO;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.model.User;
import web.mates.arriendatufinca.repository.UserRepository;
import web.mates.arriendatufinca.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ArriendatufincaApplication.class
)
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class ArriendatufincaApplicationTests {
    @Mock
    private UserRepository userRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private final List<User> user = new ArrayList<>();

    @BeforeEach
    public void setup() {
        this.user.add(User.builder()
                .id(UUID.randomUUID())
                .name("John")
                .lastName("Doe")
                .email("mail@domain.com")
                .phoneNumber("3003898475")
                .password("pass1234")
                .build());

        this.user.add(User.builder()
                .id(UUID.randomUUID())
                .name("Jane")
                .lastName("Doe")
                .email("correo@mail.com")
                .phoneNumber("3123235206")
                .password("contra1234")
                .build());
    }

    @Test
    public void UserService_RegisterUser_ReturnsUserDTO() throws Exception {
        RequestUserDTO userDTO = modelMapper.map(user, RequestUserDTO.class);

        given(userRepository.save(Mockito.any(User.class))).willReturn(user.get(0));
        given(userRepository.findById(Mockito.any(UUID.class))).willReturn(Optional.ofNullable(user.get(0)));

        UserDTO savedUser = userService.newUser(userDTO);
        Assertions.assertThat(savedUser).isNotNull();
    }
}
