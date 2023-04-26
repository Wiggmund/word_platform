package com.example.word_platform.service;

import com.example.word_platform.dto.question.QuestionCreateDto;
import com.example.word_platform.dto.question.QuestionUpdateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.repository.QuestionRepo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class QuestionService {
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

  public Question createQuestion(User user, Wordlist wordlist, Attribute attribute,
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

  public Question removeQuestion(Long questionId) {
    Question candidate = getQuestionById(questionId);
    log.debug("Remove question {}", candidate);
    questionRepo.delete(candidate);
    log.debug("Question was removed: {}", candidate);
    return candidate;
  }
}
