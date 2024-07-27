package com.yhsoft.board.security.provider;

import com.yhsoft.board.security.principal.JwtPrincipal;
import com.yhsoft.board.security.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.authentication.BadCredentialsException;

public class JwtProvider {

  private final SecretKey secretKey;
  private final Long expireTimeoutMs;

  public JwtProvider(JwtProperties jwtProperties) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey()));
    this.expireTimeoutMs = jwtProperties.expireTimeout();
  }

  public String generateToken(JwtPrincipal jwtPrincipal) {
    Date now = new Date();
    return Jwts.builder()
        .expiration(new Date(now.getTime() + expireTimeoutMs))
        .issuedAt(now)
        .claim("username", jwtPrincipal.getName())
        .claim("accountType", jwtPrincipal.getAccountType())
        .signWith(secretKey)
        .compact();
  }

  public JwtPrincipal verify(String jwtToken) {
    try {
      Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
          .parseSignedClaims(jwtToken);
      Claims payload = claimsJws.getPayload();
      return new JwtPrincipal(
          payload.get("username", String.class),
          payload.get("accountType", String.class)
      );
    } catch (JwtException exception) {
      throw new BadCredentialsException("Invalid JWT");
    }
  }
}
