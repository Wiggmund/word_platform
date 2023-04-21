package com.example.word_platform.user.service;

import com.example.word_platform.exception.not_found.UserNotFoundException;
import com.example.word_platform.shared.DuplicationCheckService;
import com.example.word_platform.user.dto.UserCreateDto;
import com.example.word_platform.user.UserEntity;
import com.example.word_platform.user.UserRepo;
import com.example.word_platform.user.dto.UserUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepo userRepo;
  private final DuplicationCheckService duplicationCheckService;

  public List<UserEntity> getAllUsers() {
    return userRepo.findAll();
  }

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

  public UserEntity updateUser(Long userId, UserUpdateDto dto) {
    UserEntity candidate = getUserById(userId);

    boolean isUsernameTheSame = candidate.getUsername().equalsIgnoreCase(dto.username());
    boolean isEmailTheSame = candidate.getEmail().equalsIgnoreCase(dto.email());

    if (isUsernameTheSame && isEmailTheSame) return candidate;

    if (!isUsernameTheSame && !isEmailTheSame)
      duplicationCheckService.checkUserForUsernameAndEmail(dto.username(), dto.email());

    if(!isUsernameTheSame)
      duplicationCheckService.checkUserForUsernameAndEmail(dto.username(), null);

    if(!isEmailTheSame)
      duplicationCheckService.checkUserForUsernameAndEmail(null, dto.email());

    candidate.setUsername(dto.username());
    candidate.setEmail(dto.email());

    return userRepo.save(candidate);
  }

  public UserEntity removeUser(Long userId) {
    UserEntity candidate = getUserById(userId);
    candidate.getWordlists().forEach(wordlist -> wordlist.getWords().forEach(word -> word.setWordlist(null)));
    candidate.getWords().forEach(word -> word.setUser(null));
    userRepo.delete(candidate);
    return candidate;
  }
}
