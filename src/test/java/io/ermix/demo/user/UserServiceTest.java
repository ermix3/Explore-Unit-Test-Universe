package io.ermix.demo.user;

import io.ermix.demo.TestcontainersConfiguration;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("User Service Test")
@Import(TestcontainersConfiguration.class)
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

     private User buildUser() {
        return User.builder()
                .username(faker.name().firstName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }

    @Test
    @DisplayName("Creating a new user")
    void create() {
        User user = User.builder()
                .username("user")
                .email("user@mail.fr")
                .password("password")
                .build();

        UserResponse createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals(user.getUsername(), createdUser.username());
    }


}