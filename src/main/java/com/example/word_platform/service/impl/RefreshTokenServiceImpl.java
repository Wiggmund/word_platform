package com.example.word_platform.service.impl;

import com.example.word_platform.dto.auth.RefreshTokenResponse;
import com.example.word_platform.exception.UserAuthenticationException;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.RefreshToken;
import com.example.word_platform.repository.RefreshTokenRepo;
import com.example.word_platform.security.JwtService;
import com.example.word_platform.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private static final String INVALID_REFRESH_TOKEN = "Invalid refresh token %s";
  private final RefreshTokenRepo refreshTokenRepo;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public String assignRefreshToken(AppUser user) {
    log.debug("Assigning refreshToken for user {}", user.getUsername());
    String rawRefreshToken = jwtService.generateRefreshToken(user.getId());
    String encodedRefreshToken = passwordEncoder.encode(rawRefreshToken);

    if (user.getRefreshToken() != null) {
      log.debug("User {} already has refresh token, so just update one", user.getUsername());
      user.getRefreshToken().setToken(encodedRefreshToken);
    } else {
      log.debug("User {} doesn't have any refresh token, so creating one", user.getUsername());
      user.setRefreshToken(
          RefreshToken.builder()
              .id(user.getId())
              .user(user)
              .token(encodedRefreshToken)
              .build()
      );
    }

    return rawRefreshToken;
  }

  @Override
  public RefreshTokenResponse refreshAccessToken(String oldRefreshToken) {
    log.debug("Refreshing access token using refresh token {}", oldRefreshToken);
    validateRefreshToken(oldRefreshToken);

    RefreshToken oldRefreshTokenEntity = getRefreshTokenEntity(oldRefreshToken);
    log.debug("Generating new pair of access and refresh tokens");
    String newRefreshToken = jwtService.generateRefreshToken(oldRefreshTokenEntity.getId());
    String accessToken = jwtService.generateAccessToken(
        oldRefreshTokenEntity.getUser().getUsername());

    log.debug("Updating old refresh token entity with new refresh token");
    oldRefreshTokenEntity.setToken(passwordEncoder.encode(newRefreshToken));
    refreshTokenRepo.save(oldRefreshTokenEntity);

    return RefreshTokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(newRefreshToken)
        .build();
  }

  @Override
  public void revokeRefreshToken(String refreshToken) {
    validateRefreshToken(refreshToken);
    RefreshToken currentRefreshToken = getRefreshTokenEntity(refreshToken);
    currentRefreshToken.revoke();
    refreshTokenRepo.delete(currentRefreshToken);
  }

  private RefreshToken getRefreshTokenEntity(String refreshToken) {
    Long userId = jwtService.extractUserIdFromRefreshToken(refreshToken);
    log.debug("Fetching refresh token by userId {}", userId);

    return refreshTokenRepo.findById(userId)
        .filter(candidate -> passwordEncoder.matches(refreshToken, candidate.getToken()))
        .orElseThrow(() -> new UserAuthenticationException(INVALID_REFRESH_TOKEN));
  }

  private void validateRefreshToken(String refreshToken) {
    if (!jwtService.isRefreshTokenValid(refreshToken)) {
      throw new UserAuthenticationException(INVALID_REFRESH_TOKEN);
    }
  }
}
