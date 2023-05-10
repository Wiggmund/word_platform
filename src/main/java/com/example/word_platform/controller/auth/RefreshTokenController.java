package com.example.word_platform.controller.auth;

import com.example.word_platform.dto.auth.RefreshTokenRequest;
import com.example.word_platform.dto.auth.RefreshTokenResponse;
import com.example.word_platform.service.RefreshTokenService;
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
  public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest dto) {
    return ResponseEntity.ok(refreshTokenService.refreshAccessToken(dto));
  }
}
