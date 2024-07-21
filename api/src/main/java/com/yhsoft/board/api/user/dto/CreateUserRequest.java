package com.yhsoft.board.api.user.dto;

import com.yhsoft.board.domain.user.domain.UserEntity;
import com.yhsoft.board.domain.user.domain.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateUserRequest(@NotBlank @Length(min = 4, max = 30) String username,
                                @NotBlank @Email String email,
                                @NotBlank String password) {

  public UserEntity toEntity() {
    UserEntity user = new UserEntity();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setStatus(UserStatus.REGISTER);
    return user;
  }
}
