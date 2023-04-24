package com.example.word_platform.dto.question;

import com.example.word_platform.model.Question;

public record QuestionResponseDto (
        Long id,
        String text,
        String type,
        Long attributeId,
        Long wordlistId,
        Long userId
) {
  public QuestionResponseDto(Question question) {
    this(
      question.getId(),
      question.getText(),
      question.getType(),
      question.getAnswer().getId(),
      question.getWordlist().getId(),
      question.getUser().getId()
    );
  }
}
