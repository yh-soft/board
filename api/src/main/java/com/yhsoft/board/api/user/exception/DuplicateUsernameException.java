package com.yhsoft.board.api.user.exception;

public class DuplicateUsernameException extends RuntimeException {

  public DuplicateUsernameException(String message) {
    super(message);
  }
}
