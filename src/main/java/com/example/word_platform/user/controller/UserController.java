package com.example.word_platform.user.controller;

import com.example.word_platform.user.UserEntity;
import com.example.word_platform.user.dto.UserCreateDto;
import com.example.word_platform.user.dto.UserResponseDto;
import com.example.word_platform.user.service.UserResponseDtoMapper;
import com.example.word_platform.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserResponseDtoMapper userResponseDtoMapper;

  @GetMapping("{userId}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
    UserEntity user =  userService.getUserById(userId);
    return ResponseEntity.ok(userResponseDtoMapper.apply(user));
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto dto) {
    UserEntity createdUser = userService.createUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDtoMapper.apply(createdUser));
  }
}
