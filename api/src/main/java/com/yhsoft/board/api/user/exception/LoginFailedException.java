package com.yhsoft.board.api.user.exception;

public class LoginFailedException extends RuntimeException {

  public LoginFailedException(String message) {
    super(message);
  }
}
