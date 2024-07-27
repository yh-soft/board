package com.yhsoft.board.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @param secretKey     base64로 인코딩한 문자열
 * @param expireTimeout 만료 시간 (ms)
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secretKey, Long expireTimeout) {

}
