package com.example.word_platform.service;

import com.example.word_platform.dto.auth.RefreshTokenResponse;
import com.example.word_platform.model.AppUser;

public interface RefreshTokenService {
  String assignRefreshToken(AppUser user);

  RefreshTokenResponse refreshAccessToken(String oldRefreshToken);

  void revokeRefreshToken(String refreshToken);
}
