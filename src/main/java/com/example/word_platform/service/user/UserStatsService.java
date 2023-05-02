package com.example.word_platform.service.user;

import com.example.word_platform.model.Stats;
import java.util.List;

public interface UserStatsService {
  List<Stats> getAllStatsRecordsByUser(Long userId);

  Stats removeStatsById(Long userId, Long statsId);
}
