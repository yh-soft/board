package com.yhsoft.board.api.user.service;

import com.yhsoft.board.api.user.dto.CreateUserRequest;
import com.yhsoft.board.api.user.dto.CreateUserResponse;
import com.yhsoft.board.api.user.dto.LoginUserRequest;
import com.yhsoft.board.api.user.dto.LoginUserResponse;
import com.yhsoft.board.api.user.exception.DuplicateUsernameException;
import com.yhsoft.board.api.user.exception.LoginFailedException;
import com.yhsoft.board.domain.user.dao.UserRepository;
import com.yhsoft.board.domain.user.domain.UserEntity;
import com.yhsoft.board.security.principal.JwtPrincipal;
import com.yhsoft.board.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public CreateUserResponse addUser(CreateUserRequest request) {
    if (userRepository.findByUsername(request.username()).isPresent()) {
      throw new DuplicateUsernameException(request.username() + "은 중복된 이름입니다.");
    }

    UserEntity entity = request.toEntity();
    entity.setPassword(passwordEncoder.encode(entity.getPassword()));
    UserEntity saved = userRepository.save(entity);
    return new CreateUserResponse(saved.getUsername(), saved.getCreatedAt());
  }

  public LoginUserResponse loginUser(LoginUserRequest request) {
    String username = request.username();
    UserEntity userEntity = userRepository.findByUsername(username)
        .orElseThrow(() -> new LoginFailedException(username + "로 로그인하는데 실파하였습니다."));
    String jwtToken = jwtProvider.generateToken(new JwtPrincipal(username, "USER"));
    if (!passwordEncoder.matches(request.password(), userEntity.getPassword())) {
      throw new LoginFailedException(username + "로 로그인하는데 실파하였습니다.");
    }
    return new LoginUserResponse(userEntity.getUserId(), jwtToken);
  }
}
