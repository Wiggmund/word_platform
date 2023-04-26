package com.example.word_platform.service.user;

import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserUpdateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.User;
import com.example.word_platform.repository.UserRepo;
import com.example.word_platform.shared.DuplicationCheckService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
  private static final String USER_NOT_FOUND_BY_ID = "User not found by id [%s]";

  private final UserRepo userRepo;
  private final DuplicationCheckService duplicationCheckService;

  public List<User> getAllUsers() {
    log.debug("Getting all existed users");
    return userRepo.findAll();
  }

  public User getUserById(Long userId) {
    log.debug("Getting user by id {}", userId);
    return userRepo.findById(userId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId)));
  }

  public User createUser(UserCreateDto dto) {
    log.debug("Creating user...");
    duplicationCheckService.checkUserForUsernameAndEmail(dto.username(), dto.email());

    User newUser = User.builder()
        .username(dto.username())
        .email(dto.email())
        .build();

    log.debug("User was created {}", newUser);
    return userRepo.save(newUser);
  }

  public User save(User user) {
    log.debug("Saving user {}", user);
    return userRepo.save(user);
  }

  public User updateUser(Long userId, UserUpdateDto dto) {
    User candidate = getUserById(userId);
    log.debug("Updating user {}", candidate);

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

    log.debug("User was updated {}", candidate);
    return userRepo.save(candidate);
  }

  public User removeUser(Long userId) {
    User candidate = getUserById(userId);
    log.debug("Removing user {}", candidate);

    log.debug("Setting user field to null for all related wordlists");
    candidate.getWordlists()
        .forEach(wordlist -> wordlist.getWords().forEach(word -> word.setWordlist(null)));

    log.debug("Setting user field to null for all related words");
    candidate.getWords().forEach(word -> word.setUser(null));

    userRepo.delete(candidate);
    log.debug("User was removed {}", candidate);
    return candidate;
  }
}
