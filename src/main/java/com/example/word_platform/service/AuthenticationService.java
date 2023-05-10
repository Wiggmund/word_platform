package com.example.word_platform.service;

import com.example.word_platform.dto.auth.SuccessLogin;
import com.example.word_platform.dto.auth.SuccessRegistration;
import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserLoginDto;

public interface AuthenticationService {
  SuccessRegistration register(UserCreateDto dto);

  SuccessLogin authenticate(UserLoginDto dto);
}
