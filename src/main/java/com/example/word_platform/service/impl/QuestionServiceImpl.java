package com.example.word_platform.service.impl;

import com.example.word_platform.dto.question.QuestionCreateDto;
import com.example.word_platform.dto.question.QuestionUpdateDto;
import com.example.word_platform.exception.DatabaseRepositoryException;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.repository.QuestionRepo;
import com.example.word_platform.service.QuestionService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {
  private static final String QUESTION_DELETING_EXCEPTION =
      "Can't delete question cause of relationship";
  private static final String QUESTION_NOT_FOUND_BY_ID = "Question not found by id [%s]";
  private final QuestionRepo questionRepo;

  public Question getQuestionById(Long questionId) {
    log.debug("Getting question by id {}", questionId);
    return questionRepo.findById(questionId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(QUESTION_NOT_FOUND_BY_ID, questionId)));
  }

  public List<Question> getAllQuestionsByIdAndWordlist(List<Long> questionIds, Wordlist wordlist) {
    log.debug("Getting questions by ids {} and wordlist {}", questionIds, wordlist);
    return questionRepo.findAllByIdInAndWordlist(questionIds, wordlist);
  }

  public List<Question> getAllWordlistQuestions(Wordlist wordlist) {
    log.debug("Getting all questions for wordlist {}", wordlist);
    return questionRepo.findAllByWordlist(wordlist);
  }

  public Question createQuestion(AppUser user, Wordlist wordlist, Attribute attribute,
                                 QuestionCreateDto dto) {
    log.debug("Creating question...");
    Question newQuestion = Question.builder()
        .text(dto.text())
        .type(dto.type())
        .user(user)
        .wordlist(wordlist)
        .answer(attribute)
        .build();

    log.debug("Question was created {}", newQuestion);
    return questionRepo.save(newQuestion);
  }

  public Question updateQuestion(Long questionId, Attribute attribute, QuestionUpdateDto dto) {
    Question candidate = getQuestionById(questionId);
    log.debug("Updating question: {}", candidate);

    candidate.setText(dto.text());
    candidate.setType(dto.type());
    candidate.setAnswer(attribute);

    log.debug("Question was updated: {}", candidate);
    return questionRepo.save(candidate);
  }

  public Question removeQuestionById(Long questionId) {
    Question candidate = getQuestionById(questionId);

    log.debug("Removing question {}", candidate);
    try {
      questionRepo.deleteById(questionId);
      questionRepo.flush();
    } catch (DataIntegrityViolationException ex) {
      throw new DatabaseRepositoryException(QUESTION_DELETING_EXCEPTION);
    }
    log.debug("Question was removed: {}", candidate);

    return candidate;
  }
}
