package io.ermix.demo.user;

import lombok.Builder;

@Builder
public record UserResponse(Long id, String username, String email,
                           String phone) {

  public static UserResponse from(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .phone(user.getPhone())
        .build();
  }
}
