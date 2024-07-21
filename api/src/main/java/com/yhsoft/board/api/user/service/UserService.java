package com.yhsoft.board.api.user.service;

import com.yhsoft.board.api.user.dto.CreateUserRequest;
import com.yhsoft.board.api.user.dto.CreateUserResponse;
import com.yhsoft.board.api.user.dto.LoginUserRequest;
import com.yhsoft.board.api.user.dto.LoginUserResponse;
import com.yhsoft.board.api.user.exception.DuplicateUsernameException;
import com.yhsoft.board.api.user.exception.LoginFailedException;
import com.yhsoft.board.domain.user.dao.UserRepository;
import com.yhsoft.board.domain.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public CreateUserResponse addUser(CreateUserRequest request) {
    if (userRepository.findByUsername(request.username()).isPresent()) {
      throw new DuplicateUsernameException(request.username() + "은 중복된 이름입니다.");
    }

    UserEntity entity = request.toEntity();
    // TODO: 비밀번호는 단방향 해싱을 사용해서 저장한다.
    UserEntity saved = userRepository.save(entity);
    return new CreateUserResponse(saved.getUsername(), saved.getCreatedAt());
  }

  public LoginUserResponse loginUser(LoginUserRequest request) {
    String username = request.username();
    UserEntity userEntity = userRepository.findByUsername(username)
        .orElseThrow(() -> new LoginFailedException(username + "로 로그인하는데 실파하였습니다."));

    // TODO: 로그인의 경우 상태가 VERIFIED 경우에만 로그인을 할 수 있도록 한다.
    if (!userEntity.getPassword().equals(request.password())) {
      throw new LoginFailedException(username + "로 로그인하는데 실파하였습니다.");
    }
    return new LoginUserResponse(userEntity.getUserId());
  }
}
