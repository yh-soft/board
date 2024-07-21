package com.yhsoft.board.api.board.dto;

import com.yhsoft.board.domain.board.domain.BoardEntity;
import java.util.List;

public record ListBoardResponse(List<BoardItem> result) {

  public record BoardItem(Long boardId, String boardName) {

    public static BoardItem fromEntity(BoardEntity entity) {
      return new BoardItem(entity.getBoardId(), entity.getBoardName());
    }
  }
}
