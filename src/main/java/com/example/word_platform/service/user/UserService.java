package com.example.word_platform.service.user;

import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserUpdateDto;
import com.example.word_platform.model.User;
import java.util.List;

public interface UserService {
  List<User> getAllUsers();

  User getUserById(Long userId);

  User createUser(UserCreateDto dto);

  User updateUser(Long userId, UserUpdateDto dto);

  User removeUserById(Long userId);

  User save(User user);
}
