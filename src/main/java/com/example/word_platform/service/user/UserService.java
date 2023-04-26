package com.example.word_platform.service.user;

import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserUpdateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.User;
import com.example.word_platform.repository.UserRepo;
import com.example.word_platform.shared.DuplicationCheckService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
  private static final String USER_NOT_FOUND_BY_ID = "User not found by id [%s]";

  private final UserRepo userRepo;
  private final DuplicationCheckService duplicationCheckService;

  public List<User> getAllUsers() {
    return userRepo.findAll();
  }

  public User getUserById(Long userId) {
    return userRepo.findById(userId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId)));
  }

  public User createUser(UserCreateDto dto) {
    duplicationCheckService.checkUserForUsernameAndEmail(dto.username(), dto.email());

    User newUser = new User(dto.username(), dto.email());
    return userRepo.save(newUser);
  }

  public User save(User user) {
    return userRepo.save(user);
  }

  public User updateUser(Long userId, UserUpdateDto dto) {
    User candidate = getUserById(userId);

    boolean isUsernameTheSame = candidate.getUsername().equalsIgnoreCase(dto.username());
    boolean isEmailTheSame = candidate.getEmail().equalsIgnoreCase(dto.email());

    if (isUsernameTheSame && isEmailTheSame) {
      return candidate;
    }

    if (!isUsernameTheSame && !isEmailTheSame) {
      duplicationCheckService.checkUserForUsernameAndEmail(dto.username(), dto.email());
    }

    if (!isUsernameTheSame) {
      duplicationCheckService.checkUserForUsernameAndEmail(dto.username(), null);
    }

    if (!isEmailTheSame) {
      duplicationCheckService.checkUserForUsernameAndEmail(null, dto.email());
    }

    candidate.setUsername(dto.username());
    candidate.setEmail(dto.email());

    return userRepo.save(candidate);
  }

  public User removeUser(Long userId) {
    User candidate = getUserById(userId);
    candidate.getWordlists()
        .forEach(wordlist -> wordlist.getWords().forEach(word -> word.setWordlist(null)));
    candidate.getWords().forEach(word -> word.setUser(null));
    userRepo.delete(candidate);
    return candidate;
  }
}
