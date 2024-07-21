package com.yhsoft.board.api.board.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBoardRequest(@NotBlank String boardName) {

}
