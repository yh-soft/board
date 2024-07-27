package com.yhsoft.board.security;

import com.yhsoft.board.security.properties.JwtProperties;
import com.yhsoft.board.security.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {

  private final JwtProperties jwtProperties;

  @Bean
  public JwtProvider jwtProvider() {
    return new JwtProvider(jwtProperties);
  }
}
