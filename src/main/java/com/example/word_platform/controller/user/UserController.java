package com.example.word_platform.controller.user;

import com.example.word_platform.model.User;
import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserResponseDto;
import com.example.word_platform.dto.user.UserUpdateDto;
import com.example.word_platform.dto.dto_mappers.UserResponseDtoMapper;
import com.example.word_platform.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserResponseDtoMapper userResponseDtoMapper;

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    List<UserResponseDto> users =  userService.getAllUsers().stream()
            .map(userResponseDtoMapper)
            .toList();

    return ResponseEntity.ok(users);
  }

  @GetMapping("{userId}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
    User user =  userService.getUserById(userId);
    return ResponseEntity.ok(userResponseDtoMapper.apply(user));
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto dto) {
    User createdUser = userService.createUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDtoMapper.apply(createdUser));
  }

  @PutMapping("{userId}")
  public ResponseEntity<UserResponseDto> updateUser(
          @PathVariable Long userId,
          @RequestBody UserUpdateDto dto
  ) {
    User updatedUser =  userService.updateUser(userId, dto);
    return ResponseEntity.ok(userResponseDtoMapper.apply(updatedUser));
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<UserResponseDto> removeUser(@PathVariable Long userId) {
    User removedUser =  userService.removeUser(userId);
    return ResponseEntity.ok(userResponseDtoMapper.apply(removedUser));
  }
}