package io.ermix.demo.user;

import io.ermix.demo.config.TestcontainersConfiguration;
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

     private UserRequest buildUserRequest() {
        return UserRequest.builder()
                .username(faker.name().firstName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .phone(faker.phoneNumber().cellPhone())
                .build();
    }

    @Test
    @DisplayName("Creating a new user")
    void create() {
        UserRequest userRequest = buildUserRequest();

        UserResponse createdUser = userService.create(userRequest);

        assertNotNull(createdUser);
        assertEquals(userRequest.username(), createdUser.username());
    }


}