package com.yhsoft.board.api.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yhsoft.board.api.user.dto.CreateUserRequest;
import com.yhsoft.board.api.user.dto.CreateUserResponse;
import com.yhsoft.board.api.user.dto.LoginUserRequest;
import com.yhsoft.board.api.user.dto.LoginUserResponse;
import com.yhsoft.board.api.user.exception.DuplicateUsernameException;
import com.yhsoft.board.api.user.exception.LoginFailedException;
import com.yhsoft.board.api.user.service.UserService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(UserController.class)
@DisplayName("UserController 테스트")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  @DisplayName("회원 가입 성공")
  void addUser_withValidUser_returnOk() throws Exception {
    // Given
    given(
        userService.addUser(new CreateUserRequest("userA", "email@email.com", "1234"))).willReturn(
        new CreateUserResponse("userA", LocalDateTime.now()));
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/user/register").contentType(APPLICATION_JSON).content("""
            {"username": "userA", "email": "email@email.com", "password": "1234"}
            """));
    // Then
    perform.andExpect(status().isCreated()).andExpect(jsonPath("$.username").value("userA"))
        .andExpect(jsonPath("$.createdAt").isNotEmpty());
  }

  @Test
  @DisplayName("중복된 이름으로 인해 회원 가입 실패")
  void addUser_withSameUsername_throwError() throws Exception {
    // Given
    given(userService.addUser(new CreateUserRequest("userA", "email@email.com", "1234"))).willThrow(
        new DuplicateUsernameException("userA는 중복된 이름입니다."));
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/user/register").contentType(APPLICATION_JSON).content("""
            {"username": "userA", "email": "email@email.com", "password": "1234"}
            """));
    // Then
    perform.andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("userA는 중복된 이름입니다."));
  }

  @Test
  @DisplayName("회원 가입 시 이름의 길이가 4보다 짧은 경우에는 회원 가입 실패")
  void addUser_withUsernameLengthLessThan4_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/user/register").contentType(APPLICATION_JSON).content("""
            {"username": "abc", "email": "email@email.com", "password": "1234"}
            """));
    // Then
    perform.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("회원 가입 시 이름의 길이가 30보다 긴 경우에는 회원 가입 실패")
  void addUser_withUsernameLengthLargerThan30_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/user/register").contentType(APPLICATION_JSON).content("""
            {"username": "123456789012345678901234567890A", "email": "email@email.com", "password": "1234"}
            """));
    // Then
    perform.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("회원 가입 시 이메일 주소가 이메일 주소 형식이 아닌 경우 실패")
  void addUser_withInvalidEmailAddressFormat_throwError() throws Exception {
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/user/register").contentType(APPLICATION_JSON).content("""
            {"username": "userA", "email": "email", "password": "1234"}
            """));
    // Then
    perform.andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("로그인 성공")
  void loginUser_withValidUser_returnOk() throws Exception {
    // Given
    given(userService.loginUser(new LoginUserRequest("userA", "1234"))).willReturn(
        new LoginUserResponse(2L));
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/user/login").contentType(APPLICATION_JSON).content("""
            {"username": "userA", "password": "1234"}
            """));
    // Then
    perform.andExpect(status().isOk()).andExpect(jsonPath("$.userId").value(2L));
  }

  @Test
  @DisplayName("로그인 실패")
  void loginUser_withInvalidUser_throwError() throws Exception {
    // Given
    given(userService.loginUser(new LoginUserRequest("userA", "1234"))).willThrow(
        new LoginFailedException("userA를 찾을 수 없습니다."));
    // When
    ResultActions perform = mockMvc.perform(
        post("/api/v1/user/login").contentType(APPLICATION_JSON).content("""
            {"username": "userA", "password": "1234"}
            """));
    // Then
    perform.andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("userA를 찾을 수 없습니다."));
  }
}
