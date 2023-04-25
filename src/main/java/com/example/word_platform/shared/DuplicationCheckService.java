package com.example.word_platform.shared;

import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.exception.ResourceAlreadyExistsException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.repository.AttributeRepo;
import com.example.word_platform.repository.QuestionRepo;
import com.example.word_platform.repository.UserRepo;
import com.example.word_platform.repository.WordRepo;
import com.example.word_platform.repository.WordlistRepo;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DuplicationCheckService {
  private final UserRepo userRepo;
  private final AttributeRepo attributeRepo;
  private final WordlistRepo wordlistRepo;
  private final WordRepo wordRepo;
  private final QuestionRepo questionRepo;
  private static final String EXCEPTION_MSG = "already exists";


  public void checkAttributeForName(String name) {
    attributeRepo.findByName(name)
        .ifPresent(existed -> {
          throw new ResourceAlreadyExistsException(
              "Attribute with name [" + name + "] " + EXCEPTION_MSG);
        });
  }

  public void checkAttributesForName(List<String> names) {
    List<Attribute> attributes = attributeRepo.findAllByNameIn(names);
    if (!attributes.isEmpty()) {
      List<String> attributeNames = attributes.stream().map(Attribute::getName).toList();
      throw new ResourceAlreadyExistsException("Attribute with name ["
          + String.join(", ", attributeNames) + "] " + EXCEPTION_MSG);
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

    if (optUsername.isPresent()) {
      userRepo.findByUsername(optUsername.get())
          .ifPresent(existed -> {
            throw new ResourceAlreadyExistsException(
                "User with username [" + username + "] " + EXCEPTION_MSG);
          });

      return;
    }

    if (optEmail.isPresent()) {
      userRepo.findByEmail(optEmail.get())
          .ifPresent(existed -> {
            throw new ResourceAlreadyExistsException(
                "User with email [" + email + "] " + EXCEPTION_MSG);
          });
    }
  }

  public void checkWordlistForTitle(String title) {
    wordlistRepo.findByTitle(title)
        .ifPresent(existed -> {
          throw new ResourceAlreadyExistsException(
              "Wordlist with title [" + title + "] " + EXCEPTION_MSG);
        });
  }

  public void checkWordForAttributeValues(String wordValue, AttributeWithValuesDto wordAttributes,
                                          Wordlist wordlist) {
    Map<Attribute, String> attributes = wordAttributes.getAttributes();

    Optional<Word> candidate = wordRepo.findByWordlistAndValueAndAttributeValues(
        wordValue,
        wordlist,
        attributes.values().stream().toList()
    );

    if (candidate.isEmpty()) {
      return;
    }

    StringBuilder stringBuilder = new StringBuilder();
    String nameValueDelimiter = "=";
    String attributesDelimiter = ", ";

    String duplicatedAttributes = attributes.entrySet().stream().reduce(
        stringBuilder,
        (sb, entry) -> sb
            .append(entry.getKey().getName())
            .append(nameValueDelimiter)
            .append(entry.getValue())
            .append(attributesDelimiter),
        StringBuilder::append
    ).delete(
        stringBuilder.lastIndexOf(attributesDelimiter),
        stringBuilder.length()
    ).toString().trim();

    throw new ResourceAlreadyExistsException("Word [" + wordValue + "] "
        + "with attributes [" + duplicatedAttributes + "] " + EXCEPTION_MSG);
  }

  public void checkQuestionForUserWordlistAndAttribute(User user, Wordlist wordlist,
                                                       Attribute attribute) {
    questionRepo.findByUserAndWordlistAndAnswer(user, wordlist, attribute).ifPresent(existed -> {
      throw new ResourceAlreadyExistsException("Question [" + existed.getText()
          + "] for user [" + user.getUsername()
          + "] in wordlist [" + wordlist.getTitle()
          + "] for attribute [" + attribute.getName() + "] " + EXCEPTION_MSG);
    });
  }
}
