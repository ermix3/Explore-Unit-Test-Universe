package io.ermix.demo.user;

import io.ermix.demo.exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserResponse get(Long id) {
    return userRepository.findById(id)
        .map(UserResponse::from)
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  public List<UserResponse> getAll() {
    return userRepository.findAll().stream().map(UserResponse::from).toList();
  }

  public UserResponse create(UserRequest userRequest) {
    return UserResponse.from(userRepository.save(User.from(userRequest)));
  }

  public void update(Long id, UserRequest userRequest) {
    User updatedUser =
        userRepository.findById(id)
            .map(existingUser -> {
              existingUser.setUsername(userRequest.username());
              existingUser.setEmail(userRequest.email());
              existingUser.setPhone(userRequest.phone());
              existingUser.setPassword(userRequest.password());
              return userRepository.save(existingUser);
            })
            .orElseThrow(() -> new NotFoundException("User not found"));

    UserResponse.from(updatedUser);
  }

  public UserResponse patch(Long id, UserRequest userRequest) {
    User updatedUser =
        userRepository.findById(id)
            .map(existingUser -> {
              if (userRequest.username() != null) {
                existingUser.setUsername(userRequest.username());
              }
              if (userRequest.email() != null) {
                existingUser.setEmail(userRequest.email());
              }
              if (userRequest.phone() != null) {
                existingUser.setPhone(userRequest.phone());
              }
              if (userRequest.password() != null) {
                existingUser.setPassword(userRequest.password());
              }
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
