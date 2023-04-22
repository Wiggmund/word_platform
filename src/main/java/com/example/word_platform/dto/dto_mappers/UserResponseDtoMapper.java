package com.example.word_platform.dto.dto_mappers;

import com.example.word_platform.model.User;
import com.example.word_platform.dto.user.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserResponseDtoMapper implements Function<User, UserResponseDto> {
  @Override
  public UserResponseDto apply(User user) {
    return new UserResponseDto(
            user.getUsername(),
            user.getEmail()
    );
  }
}
