package com.example.word_platform.service.impl.user;

import com.example.word_platform.dto.user.UserCreateDto;
import com.example.word_platform.dto.user.UserUpdateDto;
import com.example.word_platform.exception.DatabaseRepositoryException;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.repository.UserRepo;
import com.example.word_platform.service.user.UserService;
import com.example.word_platform.shared.DuplicationCheckService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  private static final String USER_DELETING_EXCEPTION =
      "Can't delete user cause of relationship";
  private static final String USER_NOT_FOUND_BY_ID = "User not found by id [%s]";

  private final UserRepo userRepo;
  private final DuplicationCheckService duplicationCheckService;

  public List<AppUser> getAllUsers() {
    log.debug("Getting all existed users");
    return userRepo.findAll();
  }

  public AppUser getUserById(Long userId) {
    log.debug("Getting user by id {}", userId);
    return userRepo.findById(userId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(USER_NOT_FOUND_BY_ID, userId)));
  }

  public AppUser createUser(UserCreateDto dto) {
    log.debug("Creating user...");
    duplicationCheckService.checkUserForUsernameAndEmail(dto.username(), dto.email());

    AppUser newAppUser = AppUser.builder()
        .username(dto.username())
        .email(dto.email())
        .build();

    log.debug("User was created {}", newAppUser);
    return userRepo.save(newAppUser);
  }

  public AppUser save(AppUser appUser) {
    log.debug("Saving user {}", appUser);
    return userRepo.save(appUser);
  }

  public AppUser updateUser(Long userId, UserUpdateDto dto) {
    AppUser candidate = getUserById(userId);
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

  public AppUser removeUserById(Long userId) {
    AppUser candidate = getUserById(userId);

    log.debug("Removing user {}", candidate);
    try {
      userRepo.deleteById(userId);
      userRepo.flush();
    } catch (DataIntegrityViolationException ex) {
      throw new DatabaseRepositoryException(USER_DELETING_EXCEPTION);
    }
    log.debug("User was removed {}", candidate);

    return candidate;
  }
}
