package com.yhsoft.board.api.board.controller;

import com.yhsoft.board.api.board.dto.CreateBoardRequest;
import com.yhsoft.board.api.board.dto.CreateBoardResponse;
import com.yhsoft.board.api.board.dto.ListBoardResponse;
import com.yhsoft.board.api.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/board")
public class BoardController {

  private final BoardService boardService;

  @GetMapping
  public ListBoardResponse listBoard() {
    return boardService.getBoards();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CreateBoardResponse addBoard(@Validated @RequestBody CreateBoardRequest request) {
    return boardService.addBoard(request.boardName());
  }
}
