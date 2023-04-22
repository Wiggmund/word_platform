package com.example.word_platform.shared;

import com.example.word_platform.exception.ResourceAlreadyExistsException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.repository.AttributeRepo;
import com.example.word_platform.repository.UserRepo;
import com.example.word_platform.repository.WordlistRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DuplicationCheckService {
  private final UserRepo userRepo;
  private final AttributeRepo attributeRepo;
  private final WordlistRepo wordlistRepo;
  private static final String EXCEPTION_MSG = "already exists";


  public void checkAttributeForName(String name) {
    attributeRepo.findByName(name)
            .ifPresent(existed -> {
              throw new ResourceAlreadyExistsException("Attribute with name [" + name + "] " + EXCEPTION_MSG);
            });
  }
  public void checkAttributesForName(List<String> names) {
    List<Attribute> attributes = attributeRepo.findAllByNameIn(names);
    if (!attributes.isEmpty()) {
      List<String> attributeNames = attributes.stream().map(Attribute::getName).toList();
      throw new ResourceAlreadyExistsException("Attribute with name [" +
              String.join(", ", attributeNames) + "] " + EXCEPTION_MSG);
    }
  }
  public void checkUserForUsernameAndEmail(String username, String email) {
    Optional<String> optUsername = Optional.ofNullable(username);
    Optional<String> optEmail = Optional.ofNullable(email);

    if (optUsername.isPresent() && optEmail.isPresent()) {
      userRepo.findByUsernameOrEmail(username, email)
              .ifPresent(existed -> {
                throw new ResourceAlreadyExistsException(
                        "User with username [" + username + "] and email [" + email + "] " + EXCEPTION_MSG);
              });

      return;
    }

    if(optUsername.isPresent()) {
      userRepo.findByUsername(optUsername.get())
              .ifPresent(existed -> {
                throw new ResourceAlreadyExistsException(
                        "User with username [" + username + "] " + EXCEPTION_MSG);
              });

      return;
    }

    if(optEmail.isPresent())
      userRepo.findByEmail(optEmail.get())
              .ifPresent(existed -> {
                throw new ResourceAlreadyExistsException(
                        "User with email [" + email + "] " + EXCEPTION_MSG);
              });
  }

  public void checkWordlistForTitle(String title) {
    wordlistRepo.findByTitle(title)
            .ifPresent(existed -> {
              throw new ResourceAlreadyExistsException(
                      "Wordlist with title [" + title + "] " + EXCEPTION_MSG);
            });
  }
}
