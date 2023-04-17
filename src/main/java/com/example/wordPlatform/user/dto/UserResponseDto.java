package com.example.wordPlatform.user.dto;

import com.example.wordPlatform.user.UserEntity;

public record UserResponseDto(
        String username,
        String email
) {
  public UserResponseDto(UserEntity user) {
    this(
            user.getUsername(),
            user.getEmail()
    );
  }
}
