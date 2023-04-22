package com.example.word_platform.shared;

import com.example.word_platform.model.Attribute;
import com.example.word_platform.repository.AttributeRepo;
import com.example.word_platform.exception.already_exist.AttributeAlreadyExistsException;
import com.example.word_platform.exception.already_exist.UserAlreadyExistsException;
import com.example.word_platform.exception.already_exist.WordlistAlreadyExistsException;
import com.example.word_platform.repository.UserRepo;
import com.example.word_platform.repository.WordlistRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DuplicationCheckService {
  private final UserRepo userRepo;
  private final AttributeRepo attributeRepo;
  private final WordlistRepo wordlistRepo;

  public void checkAttributeForName(String name) {
    attributeRepo.findByName(name)
            .ifPresent(existed -> {
              throw new AttributeAlreadyExistsException(List.of("name"));
            });
  }
  public void checkAttributesForName(List<String> names) {
    List<Attribute> attributes = attributeRepo.findAllByNameIn(names);
    if (!attributes.isEmpty())
      throw new AttributeAlreadyExistsException(List.of("name"));
  }
  public void checkUserForUsernameAndEmail(String username, String email) {
    Optional<String> optUsername = Optional.ofNullable(username);
    Optional<String> optEmail = Optional.ofNullable(email);

    if (optUsername.isPresent() && optEmail.isPresent()) {
      userRepo.findByUsernameOrEmail(username, email)
              .ifPresent(existed -> {
                Map<String, Boolean> duplicationCheck = new HashMap<>();
                duplicationCheck.put("username", existed.getUsername().equals(username));
                duplicationCheck.put("email", existed.getEmail().equals(email));

                throw new UserAlreadyExistsException(getDuplicatedFields(duplicationCheck));
              });

      return;
    }

    if(optUsername.isPresent()) {
      userRepo.findByUsername(optUsername.get())
              .ifPresent(existed -> {
                throw new UserAlreadyExistsException(List.of("username"));
              });

      return;
    }

    if(optEmail.isPresent())
      userRepo.findByEmail(optEmail.get())
              .ifPresent(existed -> {
                throw new UserAlreadyExistsException(List.of("email"));
              });
  }

  public void checkWordlistForTitle(String title) {
    wordlistRepo.findByTitle(title)
            .ifPresent(existed -> {
              throw new WordlistAlreadyExistsException(List.of("title"));
            });
  }

  private List<String> getDuplicatedFields(Map<String, Boolean> duplicationCheck) {
    return duplicationCheck.keySet().stream()
            .filter(duplicationCheck::get)
            .toList();
  }
}
