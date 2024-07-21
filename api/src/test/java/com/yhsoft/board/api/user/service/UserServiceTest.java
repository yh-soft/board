package com.yhsoft.board.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yhsoft.board.api.user.dto.CreateUserRequest;
import com.yhsoft.board.api.user.dto.CreateUserResponse;
import com.yhsoft.board.api.user.dto.LoginUserRequest;
import com.yhsoft.board.api.user.dto.LoginUserResponse;
import com.yhsoft.board.api.user.exception.DuplicateUsernameException;
import com.yhsoft.board.api.user.exception.LoginFailedException;
import com.yhsoft.board.domain.user.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("UserService 테스트")
class UserServiceTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  void beforeEach() {
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("회원 가입을 없는 계정으로 한 경우 성공한다.")
  void registerUser_withNotExistUser_returnOk() {
    // Given
    String username = "userA";
    // When
    CreateUserResponse createUserResponse = userService.addUser(
        new CreateUserRequest(username, "email@email.com", "1234"));
    // Then
    assertThat(createUserResponse.username()).isEqualTo(username);
    assertThat(createUserResponse.createdAt()).isNotNull();
  }

  @Test
  @DisplayName("회원 가입을 기존에 가입한 계정으로 진행한 경우 실패한다.")
  void registerUser_withExistUser_throwError() {
    // Given
    String username = "userB";
    userService.addUser(new CreateUserRequest(username, "email@email.com", "1111"));
    // When & Then
    assertThatThrownBy(() -> userService.addUser(
        new CreateUserRequest(username, "email2@email2.com", "1234")
    )).isExactlyInstanceOf(DuplicateUsernameException.class);
  }

  @Test
  @DisplayName("회원가입이 된 계정으로 로그인을 시도하면 성공한다.")
  void loginUser_withValidUser_returnOk() {
    // Given
    String username = "userA";
    String password = "1234";
    userService.addUser(new CreateUserRequest(username, "email@email.com", password));
    Long userId = userRepository.findByUsername(username).orElseThrow().getUserId();
    // When
    LoginUserResponse loginUserResponse = userService.loginUser(
        new LoginUserRequest(username, password));
    // Then
    assertThat(loginUserResponse.userId()).isEqualTo(userId);
  }

  @Test
  @DisplayName("회원가입이 되어 있지 않은 계정으로 로그인을 시도하면 실패한다.")
  void loginUser_withNotRegisterUser_throwError() {
    // When & Then
    assertThatThrownBy(
        () -> userService.loginUser(new LoginUserRequest("userA", "1234"))).isExactlyInstanceOf(
        LoginFailedException.class);
  }

  @Test
  @DisplayName("회원가입은 되어 있지만 비밀번호를 잘못 입력한 경우 로그인에 실패한다.")
  void loginUser_withExistUserButInvalidPassword_throwError() {
    // Given
    String username = "userA";
    userService.addUser(new CreateUserRequest(username, "email@email.com", "1234"));
    // When & then
    assertThatThrownBy(
        () -> userService.loginUser(new LoginUserRequest(username, "111"))).isExactlyInstanceOf(
        LoginFailedException.class
    );
  }
}
