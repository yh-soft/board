package com.yhsoft.board.api.board.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.yhsoft.board.api.board.dto.CreateBoardResponse;
import com.yhsoft.board.api.board.dto.ListBoardResponse;
import com.yhsoft.board.domain.board.dao.BoardRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("BoardService 테스트")
class BoardServiceTest {

  @Autowired
  private BoardRepository boardRepository;

  @Autowired
  private BoardService boardService;

  @BeforeEach
  void beforeEach() {
    boardRepository.deleteAll();
  }

  @Test
  @DisplayName("보드 생성")
  void createBoard_whenValidInput_returnItem() {
    // Given
    String boardName = "new board";
    // When
    CreateBoardResponse createBoardResponse = boardService.addBoard(boardName);
    // Then
    assertThat(createBoardResponse).hasFieldOrPropertyWithValue("boardName", boardName);
  }

  @Test
  @DisplayName("보드 목록 조회 - 생성된 보드가 없는 경우")
  void listBoard_whenHasNoBoard_returnEmptyList() {
    // When
    ListBoardResponse boards = boardService.getBoards();
    // Then
    assertThat(boards.result()).hasSize(0);
  }

  @Test
  @DisplayName("보드 목록 조회 - 생성된 보드가 여러 개 있는 경우")
  void listBoard_whenHasMultipleBoards_returnMultipleItems() {
    // Given
    boardService.addBoard("board1");
    boardService.addBoard("board2");
    // When
    ListBoardResponse boards = boardService.getBoards();
    // Then
    assertThat(boards.result()).hasSize(2).extracting("boardName")
        .isEqualTo(List.of("board1", "board2"));
  }
}
