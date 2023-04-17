package com.example.wordPlatform.user.controller;

import com.example.wordPlatform.user.dto.UserCreateDto;
import com.example.wordPlatform.user.dto.UserResponseDto;
import com.example.wordPlatform.user.service.UserService;
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

  @GetMapping("{userId}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
    UserResponseDto user =  new UserResponseDto(userService.getUserById(userId));
    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<Long> createUser(@RequestBody UserCreateDto dto) {
    Long createdUserId = userService.createUser(dto).getId();
    return ResponseEntity.status(HttpStatus.CREATED).body(createdUserId);
  }
}
