package com.yhsoft.board.api.article.exception;

public class ArticleNotFoundException extends RuntimeException {

  public ArticleNotFoundException(String message) {
    super(message);
  }
}
