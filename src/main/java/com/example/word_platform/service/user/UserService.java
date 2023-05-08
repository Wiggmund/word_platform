package com.example.word_platform.service.user;

import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserUpdateDto;
import com.example.word_platform.model.AppUser;
import java.util.List;

public interface UserService {
  List<AppUser> getAllUsers();

  AppUser getUserById(Long userId);

  AppUser createUser(UserCreateDto dto);

  AppUser updateUser(Long userId, UserUpdateDto dto);

  AppUser removeUserById(Long userId);

  AppUser save(AppUser appUser);
}
