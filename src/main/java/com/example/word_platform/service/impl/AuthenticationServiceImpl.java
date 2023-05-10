package com.example.word_platform.service.impl;

import com.example.word_platform.dto.auth.RefreshTokenRequest;
import com.example.word_platform.dto.auth.RefreshTokenResponse;
import com.example.word_platform.dto.auth.SuccessLogin;
import com.example.word_platform.dto.auth.SuccessRegistration;
import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserLoginDto;
import com.example.word_platform.exception.UserAuthenticationException;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.security.JwtService;
import com.example.word_platform.service.AuthenticationService;
import com.example.word_platform.service.RefreshTokenService;
import com.example.word_platform.service.RoleService;
import com.example.word_platform.service.user.UserService;
import com.example.word_platform.shared.EntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserService userService;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final RefreshTokenService refreshTokenService;
  private final EntityConverter entityConverter;

  @Override
  public SuccessRegistration register(UserCreateDto dto) {
    log.debug("Registering user. DTO: {}", dto);
    AppUser createdUser = userService.createUser(
        UserCreateDto.builder()
            .username(dto.username())
            .email(dto.email())
            .password(passwordEncoder.encode(dto.password()))
            .build()
    );

    createdUser.addRole(roleService.getDefaultRole());
    log.debug("Default role was added for user {}", createdUser);
    userService.save(createdUser);

    log.debug("Success registration");
    return SuccessRegistration.builder()
        .id(createdUser.getId())
        .username(createdUser.getUsername())
        .email(createdUser.getEmail())
        .roles(entityConverter.entityToDto(createdUser.getRoles()))
        .build();
  }

  @Override
  public SuccessLogin authenticate(UserLoginDto dto) {
    log.debug("Authenticating user. DTO: {}", dto);
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
    );

    AppUser user = userService.getUserByUsername(dto.username());

    String accessToken = jwtService.generateAccessToken(user.getUsername());
    String refreshToken = refreshTokenService.assignRefreshToken(user);
    log.debug("Access and refresh tokens for user {} was created", user);
    userService.save(user);

    log.debug("Success login");
    return SuccessLogin.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .roles(entityConverter.entityToDto(user.getRoles()))
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
