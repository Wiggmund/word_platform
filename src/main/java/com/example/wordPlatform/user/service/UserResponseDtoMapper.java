package com.example.wordPlatform.user.service;

import com.example.wordPlatform.user.UserEntity;
import com.example.wordPlatform.user.dto.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserResponseDtoMapper implements Function<UserEntity, UserResponseDto> {
  @Override
  public UserResponseDto apply(UserEntity user) {
    return new UserResponseDto(
            user.getUsername(),
            user.getEmail()
    );
  }
}
