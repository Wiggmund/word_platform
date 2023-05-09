package com.example.word_platform.controller.auth;

import com.example.word_platform.dto.auth.AuthenticationResponse;
import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserLoginDto;
import com.example.word_platform.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody UserCreateDto dto
  ) {
    return ResponseEntity.ok(authenticationService.register(dto));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody UserLoginDto dto
  ) {
    return ResponseEntity.ok(authenticationService.authenticate(dto));
  }
}
