package com.example.wordPlatform.shared;

import com.example.wordPlatform.attribute.AttributeEntity;
import com.example.wordPlatform.attribute.AttributeRepo;
import com.example.wordPlatform.exception.alreadyExist.AttributeAlreadyExistsException;
import com.example.wordPlatform.exception.alreadyExist.UserAlreadyExistsException;
import com.example.wordPlatform.exception.alreadyExist.WordAlreadyExistsException;
import com.example.wordPlatform.user.UserRepo;
import com.example.wordPlatform.word.WordRepo;
import com.example.wordPlatform.wordlist.WordlistEntity;
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

  public void checkWordForValueAndList(String wordValue, WordlistEntity wordlist) {
    wordRepo.findByValueAndWordlist(wordValue, wordlist)
            .ifPresent(existed -> {
              throw new WordAlreadyExistsException(List.of("value"));
            });
  }

  public void checkAttributeForName(String name) {
    attributeRepo.findByName(name)
            .ifPresent(existed -> {
              throw new AttributeAlreadyExistsException(List.of("name"));
            });
  }

  public void checkAttributesForName(List<String> names) {
    List<AttributeEntity> attributes = attributeRepo.findAllByNameIn(names);
    if (attributes.size() > 0)
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
