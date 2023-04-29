package com.example.word_platform.service;

import com.example.word_platform.dto.stats.StatsCreateDto;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.Stats;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.repository.StatsRepo;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StatsService {
  private final StatsRepo statsRepo;

  public List<Stats> getAllStatsRecordsByUser(User user) {
    log.debug("Getting all stats for user {}", user);
    return statsRepo.findAllByUser(user);
  }

  public List<Stats> createStatsRecords(
      User user,
      Wordlist wordlist,
      List<Question> questions,
      List<Word> words,
      List<StatsCreateDto> dto
  ) {
    log.debug("Creating stats for user {}", user);
    Map<Long, Question> questionsById = questions.stream().collect(Collectors.toMap(
        Question::getId,
        Function.identity()
    ));
    Map<Long, Word> wordsById = words.stream().collect(Collectors.toMap(
        Word::getId,
        Function.identity()
    ));

    List<Stats> newRecords = dto.stream()
        .map(item -> Stats.builder()
              .testingDate(item.date())
              .correct(item.correct())
              .user(user)
              .wordlist(wordlist)
              .word(wordsById.get(item.wordId()))
              .question(questionsById.get(item.questionId()))
              .build())
        .toList();

    log.debug("{} stats records were created", newRecords.size());
    return statsRepo.saveAll(newRecords);
  }
}
