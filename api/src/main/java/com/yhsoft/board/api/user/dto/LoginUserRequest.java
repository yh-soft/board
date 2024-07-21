package com.yhsoft.board.api.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(@NotBlank String username, @NotBlank String password) {

}
