package com.example.word_platform.service.user;

import com.example.word_platform.dto.stats.StatsCreateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.Stats;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.service.QuestionService;
import com.example.word_platform.service.StatsService;
import com.example.word_platform.service.WordService;
import com.example.word_platform.service.WordlistService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserWordlistStatsService {
  private final UserService userService;
  private final WordlistService wordlistService;
  private final StatsService statsService;
  private final QuestionService questionService;
  private final WordService wordService;

  public List<Stats> createStatsRecords(
      Long userId,
      Long wordlistId,
      List<StatsCreateDto> dto
  ) {
    User user = userService.getUserById(userId);
    Wordlist wordlist = wordlistService.getWordlistById(wordlistId);


    // Retrieve unique question ids
    List<Long> requiredQuestionIds = dto.stream()
        .collect(Collectors.toMap(
            StatsCreateDto::questionId,
            StatsCreateDto::questionId,
            (questionId1, questionId2) -> questionId1
        ))
        .keySet().stream().toList();
    List<Question> fetchedQuestions =
        questionService.getAllQuestionsByIdAndWordlist(requiredQuestionIds, wordlist);
    validateQuestions(requiredQuestionIds, fetchedQuestions);

    // Retrieve unique word ids
    List<Long> requiredWordIds = dto.stream()
        .collect(Collectors.toMap(
            StatsCreateDto::wordId,
            StatsCreateDto::wordId,
            (wordId1, wordId2) -> wordId1
        ))
        .keySet().stream().toList();
    List<Word> fetchedWords = wordService.getAllWordsByIdAndWordlist(requiredWordIds, wordlist);
    validateWords(requiredWordIds, fetchedWords);

    List<Stats> createdRecords =
        statsService.createStatsRecords(user, wordlist, fetchedQuestions, fetchedWords, dto);

    user.addStatsRecords(createdRecords);
    userService.save(user);

    wordlist.addStatsRecords(createdRecords);
    wordlistService.save(wordlist);

    return createdRecords;
  }

  private void validateQuestions(
      List<Long> requiredQuestionIds,
      List<Question> fetchedQuestions
  ) {
    if (requiredQuestionIds.size() == fetchedQuestions.size()) {
      return;
    }

    List<Long> fetchedQuestionIds = fetchedQuestions.stream().map(Question::getId).toList();

    List<String> notFoundQuestionIds = requiredQuestionIds.stream()
        .filter(requiredId -> !fetchedQuestionIds.contains(requiredId))
        .map(Object::toString)
        .toList();

    throw new ResourceNotFoundException("Questions with these ids ["
        + String.join(", ", notFoundQuestionIds) + "] not found");
  }

  private void validateWords(
      List<Long> requiredWordIds,
      List<Word> fetchedWords
  ) {
    if (requiredWordIds.size() == fetchedWords.size()) {
      return;
    }

    List<Long> fetchedWordIds = fetchedWords.stream().map(Word::getId).toList();

    List<String> notFoundWordIds = requiredWordIds.stream()
        .filter(requiredId -> !fetchedWordIds.contains(requiredId))
        .map(Object::toString)
        .toList();

    throw new ResourceNotFoundException("Words with these ids ["
        + String.join(", ", notFoundWordIds) + "] not found");
  }
}
