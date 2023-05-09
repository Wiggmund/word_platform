package com.example.word_platform.service.impl;

import com.example.word_platform.config.JwtService;
import com.example.word_platform.dto.auth.AuthenticationResponse;
import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserLoginDto;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.service.AuthenticationService;
import com.example.word_platform.service.RoleService;
import com.example.word_platform.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserService userService;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  @Override
  public AuthenticationResponse register(UserCreateDto dto) {
    AppUser createdUser = userService.createUser(
        UserCreateDto.builder()
            .username(dto.username())
            .email(dto.email())
            .password(passwordEncoder.encode(dto.password()))
            .build()
    );

    createdUser.addRole(roleService.getDefaultRole());
    userService.save(createdUser);

    String accessToken = jwtService.generateToken(createdUser);
    return AuthenticationResponse.builder()
        .accessToken(accessToken)
        .build();
  }

  @Override
  public AuthenticationResponse authenticate(UserLoginDto dto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
    );

    AppUser candidate = userService.getUserByUsername(dto.username());

    String accessToken = jwtService.generateToken(candidate);
    return AuthenticationResponse.builder()
        .accessToken(accessToken)
        .build();
  }
}
