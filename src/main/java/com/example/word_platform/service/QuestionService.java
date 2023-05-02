package com.example.word_platform.service;

import com.example.word_platform.dto.question.QuestionCreateDto;
import com.example.word_platform.dto.question.QuestionUpdateDto;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import java.util.List;

public interface QuestionService {
  Question getQuestionById(Long questionId);

  List<Question> getAllQuestionsByIdAndWordlist(List<Long> questionIds, Wordlist wordlist);

  List<Question> getAllWordlistQuestions(Wordlist wordlist);

  Question createQuestion(User user,
                          Wordlist wordlist,
                          Attribute attribute,
                          QuestionCreateDto dto);

  Question updateQuestion(Long questionId, Attribute attribute, QuestionUpdateDto dto);

  Question removeQuestionById(Long questionId);
}
