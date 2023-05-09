package com.example.word_platform.service;

import com.example.word_platform.dto.auth.AuthenticationResponse;
import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserLoginDto;

public interface AuthenticationService {
  AuthenticationResponse register(UserCreateDto dto);
  AuthenticationResponse authenticate(UserLoginDto dto);
}
