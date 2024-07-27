package com.yhsoft.board.security.principal;

import java.security.Principal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtPrincipal implements Principal {

  private final String name;
  private final String accountType;

}
