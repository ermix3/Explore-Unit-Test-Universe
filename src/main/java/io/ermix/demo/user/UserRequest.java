package io.ermix.demo.user;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserRequest(
        @Column(unique = true)
        @NotBlank(message = "Username is mandatory")
        String username,

        @Column(unique = true)
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is mandatory")
        String password,

        @Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits")
        String phone
) {
}
