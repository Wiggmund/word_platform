package com.example.word_platform.controller.user;

import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserResponseDto;
import com.example.word_platform.dto.user.UserUpdateDto;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.service.user.UserService;
import com.example.word_platform.shared.EntityConverter;
import java.util.List;
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

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final EntityConverter entityConverter;

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    List<UserResponseDto> users = userService.getAllUsers().stream()
        .map(entityConverter::entityToDto)
        .toList();

    return ResponseEntity.ok(users);
  }

  @GetMapping("{userId}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
    AppUser appUser = userService.getUserById(userId);
    return ResponseEntity.ok(entityConverter.entityToDto(appUser));
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto dto) {
    AppUser createdAppUser = userService.createUser(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(entityConverter.entityToDto(
        createdAppUser));
  }

  @PutMapping("{userId}")
  public ResponseEntity<UserResponseDto> updateUser(
      @PathVariable Long userId,
      @RequestBody UserUpdateDto dto
  ) {
    AppUser updatedAppUser = userService.updateUser(userId, dto);
    return ResponseEntity.ok(entityConverter.entityToDto(updatedAppUser));
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<UserResponseDto> removeUser(@PathVariable Long userId) {
    AppUser removedAppUser = userService.removeUserById(userId);
    return ResponseEntity.ok(entityConverter.entityToDto(removedAppUser));
  }
}
