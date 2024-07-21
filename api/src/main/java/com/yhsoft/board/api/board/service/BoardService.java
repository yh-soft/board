package com.yhsoft.board.api.board.service;

import com.yhsoft.board.api.board.dto.CreateBoardResponse;
import com.yhsoft.board.api.board.dto.ListBoardResponse;
import com.yhsoft.board.domain.board.dao.BoardRepository;
import com.yhsoft.board.domain.board.domain.BoardEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {

  private final BoardRepository boardRepository;

  @Transactional
  public CreateBoardResponse addBoard(String boardName) {
    BoardEntity board = new BoardEntity();
    board.setBoardName(boardName);
    BoardEntity save = boardRepository.save(board);

    return new CreateBoardResponse(save.getBoardId(), save.getBoardName());
  }

  public ListBoardResponse getBoards() {
    List<BoardEntity> boards = boardRepository.findAll();
    return new ListBoardResponse(
        boards.stream().map(ListBoardResponse.BoardItem::fromEntity).toList());
  }
}
