package com.example.word_platform.service.user;

import com.example.word_platform.dto.question.QuestionCreateDto;
import com.example.word_platform.dto.question.QuestionUpdateDto;
import com.example.word_platform.model.Question;
import java.util.List;

public interface UserWordlistQuestionService {
  List<Question> getAllWordlistQuestions(Long userId, Long wordlistId);

  Question createQuestion(Long userId, Long wordlistId, QuestionCreateDto dto);

  Question updateQuestion(Long userId, Long wordlistId, Long questionId, QuestionUpdateDto dto);

  Question removeQuestion(Long userId, Long wordlistId, Long questionId);
}
