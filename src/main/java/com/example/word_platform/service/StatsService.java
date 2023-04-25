package com.example.word_platform.service;

import com.example.word_platform.dto.stats.StatsCreateDto;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.Stats;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.repository.StatsRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatsService {
  private final StatsRepo statsRepo;

  public List<Stats> getAllStatsRecordsByUser(User user) {
    return statsRepo.findAllByUser(user);
  }

  public List<Stats> createStatsRecords(
          User user,
          Wordlist wordlist,
          List<Question> questions,
          List<Word> words,
          List<StatsCreateDto> dto
  ) {
    Map<Long, Question> questionsById = questions.stream().collect(Collectors.toMap(
            Question::getId,
            Function.identity()
    ));
    Map<Long, Word> wordsById = words.stream().collect(Collectors.toMap(
            Word::getId,
            Function.identity()
    ));

    List<Stats> newRecords = dto.stream()
            .map(item -> {
              Stats newRecord = new Stats(item.date(), item.correct());

              newRecord.setUser(user);
              newRecord.setWordlist(wordlist);
              newRecord.setWord(wordsById.get(item.wordId()));
              newRecord.setQuestion(questionsById.get(item.questionId()));

              return newRecord;
            })
            .toList();

    return statsRepo.saveAll(newRecords);
  }
}
