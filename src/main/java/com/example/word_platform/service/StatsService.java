package com.example.word_platform.service;

import com.example.word_platform.dto.stats.StatsCreateDto;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.Stats;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import java.util.List;

public interface StatsService {
  Stats getStatsById(Long statsId);

  List<Stats> getAllStatsRecordsByUser(User user);

  List<Stats> createStatsRecords(User user,
                                 Wordlist wordlist,
                                 List<Question> questions,
                                 List<Word> words,
                                 List<StatsCreateDto> dto);

  Stats removeStatsById(Long statsId);
}
