package com.example.word_platform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SigningKeyResolverImpl extends SigningKeyResolverAdapter {
  private final String accessTokenSecret;
  private final String refreshTokenSecret;

  public SigningKeyResolverImpl(
      @Value("${application.jwt.accessTokenSecret}") String accessTokenSecret,
      @Value("${application.jwt.refreshTokenSecret}") String refreshTokenSecret
  ) {
    this.accessTokenSecret = accessTokenSecret;
    this.refreshTokenSecret = refreshTokenSecret;
  }

  @Override
  public Key resolveSigningKey(JwsHeader header, Claims claims) {
    return StringUtils.hasText(claims.getSubject())
        ? getSignInKey(accessTokenSecret)
        : getSignInKey(refreshTokenSecret);
  }

  private Key getSignInKey(String secret) {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }
}
