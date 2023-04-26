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
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionService {
  private static final String QUESTION_NOT_FOUND_BY_ID = "Question not found by id [%s]";
  private final QuestionRepo questionRepo;

  public Question getQuestionById(Long questionId) {
    return questionRepo.findById(questionId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(QUESTION_NOT_FOUND_BY_ID, questionId)));
  }

  public List<Question> getAllQuestionsByIdAndWordlist(List<Long> questionIds, Wordlist wordlist) {
    return questionRepo.findAllByIdInAndWordlist(questionIds, wordlist);
  }

  public List<Question> getAllWordlistQuestions(Wordlist wordlist) {
    return questionRepo.findAllByWordlist(wordlist);
  }

  public Question createQuestion(User user, Wordlist wordlist, Attribute attribute,
                                 QuestionCreateDto dto) {
    Question newQuestion = new Question(dto.text(), dto.type());

    newQuestion.setUser(user);
    newQuestion.setWordlist(wordlist);
    newQuestion.setAnswer(attribute);

    return questionRepo.save(newQuestion);
  }

  public Question updateQuestion(Long questionId, Attribute attribute, QuestionUpdateDto dto) {
    Question candidate = getQuestionById(questionId);

    candidate.setText(dto.text());
    candidate.setType(dto.type());
    candidate.setAnswer(attribute);

    return questionRepo.save(candidate);
  }

  public Question removeQuestion(Long questionId) {
    Question candidate = getQuestionById(questionId);
    questionRepo.delete(candidate);
    return candidate;
  }
}
