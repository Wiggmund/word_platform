package com.example.word_platform.user.service;

import com.example.word_platform.exception.not_found.UserNotFoundException;
import com.example.word_platform.shared.DuplicationCheckService;
import com.example.word_platform.user.dto.UserCreateDto;
import com.example.word_platform.user.UserEntity;
import com.example.word_platform.user.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepo userRepo;
  private final DuplicationCheckService duplicationCheckService;

  public UserEntity getUserById(Long userId) {
    return userRepo.findById(userId)
            .orElseThrow(UserNotFoundException::new);
  }

  public UserEntity createUser(UserCreateDto dto) {
    duplicationCheckService.checkUserForUsernameAndEmail(dto.username(), dto.email());

    UserEntity newUser = new UserEntity(dto.username(), dto.email());
    return userRepo.save(newUser);
  }

  public UserEntity save(UserEntity user) {
    return userRepo.save(user);
  }
}
