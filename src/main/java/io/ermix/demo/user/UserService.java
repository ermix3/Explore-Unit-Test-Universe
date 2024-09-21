package io.ermix.demo.user;

import io.ermix.demo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse get(Long id) {
        return userRepository
                .findById(id)
                .map(UserResponse::from)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<UserResponse> getAll() {
        return userRepository
                .findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse create(User user) {
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse update(Long id, User user) {
        User updatedUser = userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPassword(user.getPassword());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new NotFoundException("User not found"));

        return UserResponse.from(updatedUser);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
