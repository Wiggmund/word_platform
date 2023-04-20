package com.example.word_platform.shared;

import com.example.word_platform.attribute.AttributeEntity;
import com.example.word_platform.attribute.AttributeRepo;
import com.example.word_platform.exception.already_exist.AttributeAlreadyExistsException;
import com.example.word_platform.exception.already_exist.UserAlreadyExistsException;
import com.example.word_platform.user.UserRepo;
import com.example.word_platform.word.WordRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DuplicationCheckService {
  private final WordRepo wordRepo;
  private final UserRepo userRepo;
  private final AttributeRepo attributeRepo;

  public void checkAttributeForName(String name) {
    attributeRepo.findByName(name)
            .ifPresent(existed -> {
              throw new AttributeAlreadyExistsException(List.of("name"));
            });
  }
  public void checkAttributesForName(List<String> names) {
    List<AttributeEntity> attributes = attributeRepo.findAllByNameIn(names);
    if (!attributes.isEmpty())
      throw new AttributeAlreadyExistsException(List.of("name"));
  }
  public void checkUserForUsernameAndEmail(String username, String email) {
    userRepo.findByUsernameOrEmail(username, email)
            .ifPresent(existed -> {
              Map<String, Boolean> duplicationCheck = new HashMap<>();
              duplicationCheck.put("username", existed.getUsername().equals(username));
              duplicationCheck.put("email", existed.getEmail().equals(email));

              throw new UserAlreadyExistsException(getDuplicatedFields(duplicationCheck));
            });
  }

  private List<String> getDuplicatedFields(Map<String, Boolean> duplicationCheck) {
    return duplicationCheck.keySet().stream()
            .filter(duplicationCheck::get)
            .toList();
  }
}
