package com.yhsoft.board.api.user.dto;

import java.time.LocalDateTime;

public record CreateUserResponse(String username, LocalDateTime createdAt) {

}
