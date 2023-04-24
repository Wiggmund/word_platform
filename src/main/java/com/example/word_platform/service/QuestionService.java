package com.example.word_platform.service;

import com.example.word_platform.dto.question.QuestionCreateDto;
import com.example.word_platform.dto.question.QuestionUpdateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.repository.QuestionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {
  private final QuestionRepo questionRepo;

  public Question getQuestionById(Long questionId) {
    return questionRepo.findById(questionId).orElseThrow(() ->
            new ResourceNotFoundException("Question with id [" + questionId + "] not found"));
  }

  public List<Question> getAllWordlistQuestions(Wordlist wordlist) {
    return questionRepo.findAllByWordlist(wordlist);
  }

  public Question createQuestion(User user, Wordlist wordlist, Attribute attribute, QuestionCreateDto dto) {
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