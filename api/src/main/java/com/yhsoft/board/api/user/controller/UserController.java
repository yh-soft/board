package com.yhsoft.board.api.user.controller;

import com.yhsoft.board.api.user.dto.CreateUserRequest;
import com.yhsoft.board.api.user.dto.CreateUserResponse;
import com.yhsoft.board.api.user.dto.LoginUserRequest;
import com.yhsoft.board.api.user.dto.LoginUserResponse;
import com.yhsoft.board.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public CreateUserResponse createUser(@Validated @RequestBody CreateUserRequest request) {
    return userService.addUser(request);
  }

  @PostMapping("/login")
  public LoginUserResponse loginUser(@Validated @RequestBody LoginUserRequest request) {
    return userService.loginUser(request);
  }
}
