package com.example.word_platform.service.user;

import com.example.word_platform.dto.question.QuestionCreateDto;
import com.example.word_platform.dto.question.QuestionUpdateDto;
import com.example.word_platform.exception.WordlistAttributesException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.service.AttributeService;
import com.example.word_platform.service.QuestionService;
import com.example.word_platform.service.WordlistService;
import com.example.word_platform.shared.DuplicationCheckService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserWordlistQuestionService {
  private final UserService userService;
  private final WordlistService wordlistService;
  private final AttributeService attributeService;
  private final QuestionService questionService;
  private final DuplicationCheckService duplicationCheckService;

  private record EntityValidationsResult(
        User user,
        Wordlist wordlist
  ) {}

  public List<Question> getAllWordlistQuestions(
          Long userId,
          Long wordlistId
  ) {
    EntityValidationsResult entities = checkEntitiesPresence(userId, wordlistId);
    return questionService.getAllWordlistQuestions(entities.wordlist());
  }

  public Question createQuestion(
          Long userId,
          Long wordlistId,
          QuestionCreateDto dto
  ) {
    EntityValidationsResult entities = checkEntitiesPresence(userId, wordlistId);
    User user = entities.user();
    Wordlist wordlist = entities.wordlist();
    Attribute attribute = attributeService.getAttributeById(dto.attributeId());

    checkAttributeAndForDuplication(user, wordlist, attribute);

    Question createdQuestion = questionService.createQuestion(user, wordlist, attribute, dto);
    user.addQuestion(createdQuestion);
    userService.save(user);
    wordlist.addQuestion(createdQuestion);
    wordlistService.save(wordlist);

    return createdQuestion;
  }

  public Question updateQuestion(
          Long userId,
          Long wordlistId,
          Long questionId,
          QuestionUpdateDto dto
  ) {
    EntityValidationsResult entities = checkEntitiesPresence(userId, wordlistId);
    User user = entities.user();
    Wordlist wordlist = entities.wordlist();
    Attribute attribute = attributeService.getAttributeById(dto.attributeId());

    checkAttributeAndForDuplication(user, wordlist, attribute);

    return questionService.updateQuestion(
            questionId,
            attribute,
            dto
    );
  }

  public Question removeQuestion(
          Long userId,
          Long wordlistId,
          Long questionId
  ) {
    checkEntitiesPresence(userId, wordlistId);
    return questionService.removeQuestion(questionId);
  }

  private void checkAttributeAndForDuplication(
          User user,
          Wordlist wordlist,
          Attribute attribute
  ) {
    if (!wordlist.getAttributes().contains(attribute))
      throw new WordlistAttributesException("Wordlist [" + wordlist.getTitle()
              + "] doesn't support [" + attribute.getName() + "] attribute");

    duplicationCheckService.checkQuestionForUserWordlistAndAttribute(user, wordlist, attribute);
  }

  private EntityValidationsResult checkEntitiesPresence(
          Long userId,
          Long wordlistId
  ) {
      return new EntityValidationsResult(
        userService.getUserById(userId),
        wordlistService.getWordlistById(wordlistId)
      );
  }
}
