package com.yhsoft.board.api.user.advice;

import com.yhsoft.board.api.global.dto.GlobalErrorResponse;
import com.yhsoft.board.api.user.exception.DuplicateUsernameException;
import com.yhsoft.board.api.user.exception.LoginFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

  @ExceptionHandler(DuplicateUsernameException.class)
  public ResponseEntity<GlobalErrorResponse> duplicateName(Exception e) {
    return new ResponseEntity<>(
        new GlobalErrorResponse("UE001", e.getMessage()),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(LoginFailedException.class)
  public ResponseEntity<GlobalErrorResponse> loginFailed(Exception e) {
    return new ResponseEntity<>(
        new GlobalErrorResponse("UE002", e.getMessage()),
        HttpStatus.NOT_FOUND
    );
  }
}
