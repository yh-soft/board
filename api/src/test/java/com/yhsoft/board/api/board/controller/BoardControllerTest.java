package com.yhsoft.board.api.board.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yhsoft.board.api.board.dto.CreateBoardResponse;
import com.yhsoft.board.api.board.dto.ListBoardResponse;
import com.yhsoft.board.api.board.service.BoardService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(BoardController.class)
@DisplayName("BoardController 테스트")
class BoardControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BoardService boardService;

  @Test
  @DisplayName("boardName이 없이 보드 생성 요청을 보내는 경우 에러를 발생")
  void addBoardRequest_withoutBoardName_raiseError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/board").contentType(APPLICATION_JSON).content("""
            {"item": "123"}
            """));
    // Then
    perform.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("boardName의 값이 비어있는 경우 에러를 발생")
  void addBoardRequest_withEmptyBoardName_raiseError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/board").contentType(APPLICATION_JSON).content("""
            {"boardName": "  "}
            """));
    // Then
    perform.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("boardName의 값이 적절한 경우 제대로 된 응답을 전송")
  void addBoardRequest_withNonEmptyBoardName_returnItem() throws Exception {
    // Given
    given(boardService.addBoard("new board")).willReturn(new CreateBoardResponse(1L, "new board"));
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/board").contentType(APPLICATION_JSON).content("""
            {"boardName": "new board"}
            """));
    // Then
    perform.andExpect(status().isCreated()).andExpect(jsonPath("$.boardId").value(1L))
        .andExpect(jsonPath("$.boardName").value("new board"));
  }

  @Test
  @DisplayName("board 목록이 없는 경우 빈 배열을 반환")
  void getBoards_withEmptyBoard_returnEmptyList() throws Exception {
    // Given
    given(boardService.getBoards()).willReturn(new ListBoardResponse(List.of()));
    // When
    ResultActions perform = mockMvc.perform(get("/api/v1/board"));
    // Then
    perform.andExpect(status().isOk()).andExpect(jsonPath("$.result").isArray())
        .andExpect(jsonPath("$.result").isEmpty());
  }

  @Test
  @DisplayName("board 목록이 있는 경우 해당 목록을 반환")
  void getBoards_withMultipleBoards_returnList() throws Exception {
    // Given
    given(boardService.getBoards()).willReturn(new ListBoardResponse(List.of(
        new ListBoardResponse.BoardItem(1L, "board1"),
        new ListBoardResponse.BoardItem(2L, "board2")
    )));
    // When
    ResultActions perform = mockMvc.perform(get("/api/v1/board"));
    // Then
    perform.andExpect(status().isOk()).andExpect(jsonPath("$.result").isArray())
        .andExpect(jsonPath("$.result[0].boardId").value(1L))
        .andExpect(jsonPath("$.result[0].boardName").value("board1"))
        .andExpect(jsonPath("$.result[1].boardId").value(2L))
        .andExpect(jsonPath("$.result[1].boardName").value("board2"));
  }
}
