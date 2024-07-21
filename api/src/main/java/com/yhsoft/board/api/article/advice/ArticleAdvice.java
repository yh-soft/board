package com.yhsoft.board.api.article.advice;

import com.yhsoft.board.api.article.exception.ArticleNotFoundException;
import com.yhsoft.board.api.global.dto.GlobalErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ArticleAdvice {

  @ExceptionHandler(ArticleNotFoundException.class)
  public ResponseEntity<GlobalErrorResponse> articleNotFound(Exception e) {
    return new ResponseEntity<>(new GlobalErrorResponse("AE001", e.getMessage()),
        HttpStatus.NOT_FOUND);
  }
}
