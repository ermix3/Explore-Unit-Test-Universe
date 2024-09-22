package io.ermix.demo.user;

import io.ermix.demo.config.TestcontainersConfiguration;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("User Repository Test")
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

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
    @DisplayName("Should save user")
    void shouldSaveUser() {
        User user = buildUser();
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(user.getUsername(), savedUser.getUsername());
    }

    @Test
    @DisplayName("Should find user by id")
    void shouldFindUserById() {
        User user = buildUser();

        User savedUser = userRepository.save(user);

        User foundUser = userRepository
                .findById(savedUser.getId())
                .orElse(null);

        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {
        User user = buildUser();
        User savedUser = userRepository.save(user);

        savedUser.setUsername("new user");
        User updatedUser = userRepository.save(savedUser);

        assertEquals(savedUser.getId(), updatedUser.getId());
        assertEquals(savedUser.getUsername(), updatedUser.getUsername());
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        User user = buildUser();
        User savedUser = userRepository.save(user);

        userRepository.delete(savedUser);

        User deletedUser = userRepository
                .findById(savedUser.getId())
                .orElse(null);

        assertNull(deletedUser);
    }
}