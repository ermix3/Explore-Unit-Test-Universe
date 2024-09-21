package io.ermix.demo.user;

import io.ermix.demo.TestcontainersConfiguration;
import io.ermix.demo.exception.handler.ErrorResponse;
import net.datafaker.Faker;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("User Controller Test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestRestTemplate restTemplate;

    Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    private @NotNull User getUser() {
        return userRepository
                .save(
                        buildUser(
                                faker.name().firstName(),
                                faker.internet().emailAddress(),
                                faker.internet().password()
                        )
                );
    }

    private static User buildUser(String username, String email, String password) {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

    @Test
    void shouldCreateNewUser() {
        User user = buildUser(faker.name().firstName(),
                faker.internet().emailAddress(),
                faker.internet().password());

        ResponseEntity<UserResponse> response = restTemplate.postForEntity("/api/v1/users", user, UserResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
    }

    @Test
    void shouldFindAllPosts() {
        List<User> newUsers = List.of(
                getUser(),
                buildUser("bob", "bob@mail.fr", "bob")
        );
        userRepository.saveAll(newUsers);

        User[] users = restTemplate.getForObject("/api/v1/users", User[].class);
        assertThat(users).isNotNull();
        assertThat(users.length).isEqualTo(2);
    }

    @Test
    void shouldFindPostWhenValidPostID() {
        User savedUser = getUser();
        ResponseEntity<UserResponse> response = restTemplate.getForEntity("/api/v1/users/" + savedUser.getId(), UserResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEquals(savedUser.getUsername(), response.getBody().username());
    }

    @Test
    void shouldThrowNotFoundWhenInvalidUserID() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/api/v1/users/" + new Random().nextLong(), ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
//    @Disabled
    void update() {
        User savedUser = getUser();
        User updatedUser = User.builder()
                .username("alice2")
                .email("alice2@mail.com")
                .password("alice2")
                .build();

        restTemplate.put("/api/v1/users/" + savedUser.getId(), updatedUser);

        User findedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(findedUser.getUsername()).isEqualTo(updatedUser.getUsername());
        assertThat(findedUser.getEmail()).isEqualTo(updatedUser.getEmail());
        assertThat(findedUser.getPassword()).isEqualTo(updatedUser.getPassword());
    }

    @Test
    void shouldDeleteWithValidID() {
        User savedUser = getUser();
        ResponseEntity<Void> response = restTemplate
                .exchange("/api/v1/users/" + savedUser.getId(), HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldDeleteWithInvalidID() {
        ResponseEntity<ErrorResponse> response = restTemplate
                .exchange("/api/v1/users/" + new Random().nextLong(), HttpMethod.DELETE, null, ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}