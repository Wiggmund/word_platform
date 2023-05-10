package com.example.word_platform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class JwtService {
  private static final String USER_ID = "user_id";
  private final String accessTokenSecret;
  private final String refreshTokenSecret;
  private final Integer accessExpirationTimeInMinutes;
  private final Integer refreshExpirationTimeInDays;
  private final SigningKeyResolverImpl signingKeyResolver;

  @Autowired
  public JwtService(
      @Value("${application.jwt.accessTokenSecret}") String accessTokenSecret,
      @Value("${application.jwt.refreshTokenSecret}") String refreshTokenSecret,
      @Value("${application.jwt.accessExpirationTimeInMinutes}") Integer accessExpirationTimeInMinutes,
      @Value("${application.jwt.refreshExpirationTimeInDays}") Integer refreshExpirationTimeInDays,
      SigningKeyResolverImpl signingKeyResolver
  ) {
    this.accessTokenSecret = accessTokenSecret;
    this.refreshTokenSecret = refreshTokenSecret;
    this.accessExpirationTimeInMinutes = accessExpirationTimeInMinutes;
    this.refreshExpirationTimeInDays = refreshExpirationTimeInDays;
    this.signingKeyResolver = signingKeyResolver;
  }

  public String generateAccessToken(String username) {
    log.debug("Generating accessToken for user {}...", username);
    Calendar calendar = getCalendar();
    calendar.add(Calendar.MINUTE, accessExpirationTimeInMinutes);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(calendar.getTime())
        .signWith(getSignInKey(accessTokenSecret), SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateRefreshToken(Long userId) {
    log.debug("Generating refreshToken for userId {}...", userId);
    Calendar calendar = getCalendar();
    calendar.add(Calendar.DATE, refreshExpirationTimeInDays);

    Claims claims = Jwts.claims();
    claims.put(USER_ID, userId);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date())
        .setExpiration(calendar.getTime())
        .signWith(getSignInKey(refreshTokenSecret), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isAccessTokenValid(String accessToken) {
    log.debug("Validating accessToken {}", accessToken);
    return isTokenValid(accessTokenSecret, accessToken);
  }

  public boolean isRefreshTokenValid(String refreshToken) {
    log.debug("Validating refreshToken {}", refreshToken);
    return isTokenValid(refreshTokenSecret, refreshToken);
  }

  public String extractUsernameFromAccessToken(String jwtToken) {
    return extractAllClaims(jwtToken).getSubject();
  }

  public Long extractUserIdFromRefreshToken(String refreshToken) {
    return extractAllClaims(refreshToken).get(USER_ID, Long.class);
  }

  private Date extractExpiration(String jwtToken) {
    return extractAllClaims(jwtToken).getExpiration();
  }

  private Claims extractAllClaims(String jwtToken) {
    return Jwts.parserBuilder()
        .setSigningKeyResolver(signingKeyResolver)
        .build()
        .parseClaimsJws(jwtToken)
        .getBody();
  }

  private boolean isTokenExpired(String jwtToken) {
    return extractExpiration(jwtToken).before(new Date());
  }

  private boolean isTokenValid(String secret, String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSignInKey(secret)).build()
          .parseClaimsJws(token);

      if (isTokenExpired(token)) {
        log.debug("Token has been expired");
        return false;
      }

      log.debug("Token is valid");
      return true;
    } catch (JwtException ex) {
      log.debug("Token is invalid");
      return false;
    }
  }

  private Key getSignInKey(String secret) {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  private Calendar getCalendar() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    return calendar;
  }

  public String getJwtFromRequest(HttpServletRequest request) {
    log.debug("Getting bearer token from request");
    final String bearerToken = request.getHeader("Authorization");

    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      log.debug("Bearer token is present in request");
      return bearerToken.substring(7);
    }

    log.debug("Bearer token is not present in request");
    return null;
  }
}
