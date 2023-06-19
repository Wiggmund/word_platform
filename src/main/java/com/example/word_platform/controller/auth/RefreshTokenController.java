package com.example.word_platform.controller.auth;

import com.example.word_platform.dto.auth.RefreshTokenRequest;
import com.example.word_platform.dto.auth.RefreshTokenResponse;
import com.example.word_platform.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth/token")
@RequiredArgsConstructor
public class RefreshTokenController {
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/refresh")
  public ResponseEntity<RefreshTokenResponse> refresh(
      @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok(
        refreshTokenService.refreshAccessToken(refreshTokenRequest.refreshToken()));
  }

  @PostMapping("/revoke")
  public void revoke(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    refreshTokenService.revokeRefreshToken(refreshTokenRequest.refreshToken());
  }
}
