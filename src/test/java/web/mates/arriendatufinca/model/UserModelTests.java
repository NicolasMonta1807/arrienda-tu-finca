package web.mates.arriendatufinca.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import web.mates.arriendatufinca.ArriendatufincaApplication;
import web.mates.arriendatufinca.dto.UserDTO;
import web.mates.arriendatufinca.helper.TestVariables;

@SpringBootTest(classes = ArriendatufincaApplication.class)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserModelTests {
    @Autowired
    ModelMapper modelmapper;

    private static final User testUser = TestVariables.users.get(0);

    private static UserDTO testUserDTO;

    @BeforeAll
    public static void setup() {
        testUserDTO = UserDTO.builder()
                .id(testUser.getId())
                .name(testUser.getName())
                .lastName(testUser.getLastName())
                .email(testUser.getEmail())
                .phoneNumber(testUser.getPhoneNumber())
                .build();
    }

    @Test
    void UserMode_TestProperties() {
        User user = User.builder()
                .id(testUser.getId())
                .name(testUser.getName())
                .lastName(testUser.getLastName())
                .phoneNumber(testUser.getPhoneNumber())
                .password(testUser.getPassword())
                .email(testUser.getEmail())
                .properties(testUser.getProperties())
                .bookings(testUser.getBookings())
                .reviews(testUser.getReviews())
                .build();

        Assertions.assertThat(user)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testUser);
    }

    @Test
    void UserDTO_TestMapping() {
        UserDTO userDTO = modelmapper.map(testUser, UserDTO.class);

        Assertions.assertThat(userDTO)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(testUserDTO);
    }
}
