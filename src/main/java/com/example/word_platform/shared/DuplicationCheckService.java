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
  private static final String ATTRIBUTE_ALREADY_EXISTS = "Attribute [%s] already exists";
  private static final String USER_ALREADY_EXISTS = "User [%s] with email [%s] already exists";
  private static final String EMAIL_ALREADY_EXISTS = "Email [%s] already exists";
  private static final String USERNAME_ALREADY_EXISTS = "Username [%s] already exists";
  private static final String WORDLIST_ALREADY_EXISTS = "Wordlist [%s] already exists";
  private static final String WORD_ALREADY_EXISTS = "Word [%s] with [%s] attributes already exists";
  private static final String QUESTION_ALREADY_EXISTS =
      "Question [%s] for [%s] attribute already exists";

  private final UserRepo userRepo;
  private final AttributeRepo attributeRepo;
  private final WordlistRepo wordlistRepo;
  private final WordRepo wordRepo;
  private final QuestionRepo questionRepo;

  public void checkAttributeForName(String name) {
    attributeRepo.findByName(name)
        .ifPresent(existed -> {
          throw new ResourceAlreadyExistsException(String.format(ATTRIBUTE_ALREADY_EXISTS, name));
        });
  }

  public void checkAttributesForName(List<String> names) {
    List<Attribute> attributes = attributeRepo.findAllByNameIn(names);
    if (!attributes.isEmpty()) {
      List<String> attributeNames = attributes.stream().map(Attribute::getName).toList();
      throw new ResourceAlreadyExistsException(String.format(
          ATTRIBUTE_ALREADY_EXISTS,
          String.join(", ", attributeNames)
      ));
    }
  }

  public void checkUserForUsernameAndEmail(String username, String email) {
    Optional<String> optUsername = Optional.ofNullable(username);
    Optional<String> optEmail = Optional.ofNullable(email);

    if (optUsername.isPresent() && optEmail.isPresent()) {
      userRepo.findByUsernameOrEmail(username, email)
          .ifPresent(existed -> {
            throw new ResourceAlreadyExistsException(
                String.format(USER_ALREADY_EXISTS, username, email));
          });

      return;
    }

    if (optUsername.isPresent()) {
      userRepo.findByUsername(optUsername.get())
          .ifPresent(existed -> {
            throw new ResourceAlreadyExistsException(
                String.format(USERNAME_ALREADY_EXISTS, username));
          });

      return;
    }

    optEmail.flatMap(userRepo::findByEmail).ifPresent(existed -> {
      throw new ResourceAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS, email));
    });
  }

  public void checkWordlistForTitle(String title) {
    wordlistRepo.findByTitle(title)
        .ifPresent(existed -> {
          throw new ResourceAlreadyExistsException(String.format(WORDLIST_ALREADY_EXISTS, title));
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

    throw new ResourceAlreadyExistsException(
        String.format(WORD_ALREADY_EXISTS, wordValue, duplicatedAttributes));
  }

  public void checkQuestionForUserWordlistAndAttribute(User user, Wordlist wordlist,
                                                       Attribute attribute) {
    questionRepo.findByUserAndWordlistAndAnswer(user, wordlist, attribute).ifPresent(existed -> {
      throw new ResourceAlreadyExistsException(
          String.format(QUESTION_ALREADY_EXISTS, existed.getText(), attribute.getName()));
    });
  }
}
