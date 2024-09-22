package io.ermix.demo.user;

import io.ermix.demo.config.TestcontainersConfiguration;
import io.ermix.demo.exception.handler.ErrorResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static io.ermix.demo.utils.ItemBuilder.buildUserRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("User Controller Test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    final String baseUrl = "/api/v1/users";

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    private @NotNull User getUser() {
        return userRepository.save(User.from(buildUserRequest()));
    }

    @Test
    @DisplayName("Should create new user")
    void shouldCreateNewUser() {
        UserRequest userRequest = buildUserRequest();

        ResponseEntity<UserResponse> response = restTemplate.postForEntity(baseUrl, userRequest, UserResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {
        List<User> newUsers = IntStream.range(0, 20)
                .mapToObj(i -> User.from(buildUserRequest()))
                .toList();
        userRepository.saveAll(newUsers);

        UserResponse[] users = restTemplate.getForObject(baseUrl, UserResponse[].class);
        assertThat(users).isNotNull();
        assertThat(users.length).isEqualTo(20);
    }

    @Test
    @DisplayName("Should find user when valid user ID")
    void shouldFindUserWhenValidUserID() {
        User savedUser = getUser();
        ResponseEntity<UserResponse> response = restTemplate.getForEntity(baseUrl + "/" + savedUser.getId(), UserResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertEquals(savedUser.getUsername(), response.getBody().username());
    }

    @Test
    @DisplayName("Should throw not found when invalid user ID")
    void shouldThrowNotFoundWhenInvalidUserID() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(baseUrl + "/" + new Random().nextLong(), ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
//    @Disabled
    @DisplayName("Should update user with valid ID")
    void update() {
        User savedUser = getUser();

        UserRequest updatedUser = buildUserRequest();
        restTemplate.put(baseUrl + "/" + savedUser.getId(), updatedUser);

        User findedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(findedUser.getUsername()).isEqualTo(updatedUser.username());
        assertThat(findedUser.getEmail()).isEqualTo(updatedUser.email());
        assertThat(findedUser.getPassword()).isEqualTo(updatedUser.password());
    }

    @Test
//    @Disabled
    @DisplayName("Should patch user with valid ID")
    void patch() {
        User savedUser = getUser();

        UserRequest userRequest = buildUserRequest();
//        UserResponse patchUser = restTemplate.patchForObject(baseUrl + "/" + savedUser.getId(),  new HttpEntity<>(userRequest), UserResponse.class);
        ResponseEntity<UserResponse> response = restTemplate.exchange(baseUrl + "/" + savedUser.getId(), HttpMethod.PATCH, new HttpEntity<>(userRequest), UserResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserResponse patchUser = response.getBody();
        assertThat(patchUser).isNotNull();
        assertThat(patchUser.username()).isEqualTo(userRequest.username());
        assertThat(patchUser.email()).isEqualTo(userRequest.email());
        assertThat(patchUser.phone()).isEqualTo(userRequest.phone());
    }

    @Test
    @DisplayName("Should delete user with valid ID")
    void shouldDeleteWithValidID() {
        User savedUser = getUser();
        ResponseEntity<Void> response = restTemplate
                .exchange(baseUrl + "/" + savedUser.getId(), HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Should throw not found when invalid ID")
    void shouldThrowNotFoundWhenDeleteWithInvalidID() {
        ResponseEntity<ErrorResponse> response = restTemplate
                .exchange(baseUrl + "/" + new Random().nextLong(), HttpMethod.DELETE, null, ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    // **Validation Tests**

    @Test
    @DisplayName("Should return 400 Bad Request when creating user with invalid data")
    void shouldReturnBadRequestWhenCreatingUserWithInvalidData() {
        UserRequest invalidUserRequest = new UserRequest("", "invalid-email", "123", null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(invalidUserRequest, headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(baseUrl, requestEntity, ErrorResponse.class);

        // Assert that the response status is 400 Bad Request
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Assert that the response body contains validation errors
        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.details()).contains(
                "Username is mandatory",
                "Email should be valid"
        );
    }

    @Test
    @DisplayName("Should return 400 Bad Request when updating user with invalid data")
    void shouldReturnBadRequestWhenUpdatingUserWithInvalidData() {
        User savedUser = getUser();

        UserRequest invalidUserRequest = new UserRequest("", "invalid-email", "123", null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(invalidUserRequest, headers);

        // Perform PUT request to update the user
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                baseUrl + "/" + savedUser.getId(),
                HttpMethod.PUT,
                requestEntity,
                ErrorResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ErrorResponse errorResponse = response.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(errorResponse.details()).contains(
                "Username is mandatory",
                "Email should be valid"
        );
    }
}