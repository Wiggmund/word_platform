package com.example.word_platform.controller.user;

import com.example.word_platform.dto.question.QuestionCreateDto;
import com.example.word_platform.dto.question.QuestionResponseDto;
import com.example.word_platform.dto.question.QuestionUpdateDto;
import com.example.word_platform.model.Question;
import com.example.word_platform.service.user.UserWordlistQuestionService;
import com.example.word_platform.shared.EntityConverter;
import java.util.List;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/{userId}/wordlists/{wordlistId}/questions")
@AllArgsConstructor
public class UserWordlistQuestionController {
  private final UserWordlistQuestionService userWordlistQuestionService;
  private final EntityConverter entityConverter;

  @GetMapping
  public ResponseEntity<List<QuestionResponseDto>> getAllWordlistQuestions(
      @PathVariable Long userId,
      @PathVariable Long wordlistId
  ) {
    List<QuestionResponseDto> questions =
        userWordlistQuestionService.getAllWordlistQuestions(userId, wordlistId)
            .stream()
            .map(entityConverter::entityToDto)
            .toList();
    return ResponseEntity.ok(questions);
  }

  @PostMapping
  public ResponseEntity<QuestionResponseDto> createQuestion(
      @PathVariable Long userId,
      @PathVariable Long wordlistId,
      @Valid @RequestBody QuestionCreateDto dto
  ) {
    Question createdQuestion = userWordlistQuestionService.createQuestion(userId, wordlistId, dto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(entityConverter.entityToDto(createdQuestion));
  }

  @PutMapping("{questionId}")
  public ResponseEntity<QuestionResponseDto> updateQuestion(
      @PathVariable Long userId,
      @PathVariable Long wordlistId,
      @PathVariable Long questionId,
      @Valid @RequestBody QuestionUpdateDto dto
  ) {
    Question updatedQuestion =
        userWordlistQuestionService.updateQuestion(userId, wordlistId, questionId, dto);
    return ResponseEntity.ok(entityConverter.entityToDto(updatedQuestion));
  }

  @DeleteMapping("{questionId}")
  public ResponseEntity<QuestionResponseDto> removeQuestion(
      @PathVariable Long userId,
      @PathVariable Long wordlistId,
      @PathVariable Long questionId
  ) {
    Question removedQuestion =
        userWordlistQuestionService.removeQuestion(userId, wordlistId, questionId);
    return ResponseEntity.ok(entityConverter.entityToDto(removedQuestion));
  }
}
