package com.example.word_platform.service.user;

import com.example.word_platform.model.Stats;
import com.example.word_platform.model.User;
import com.example.word_platform.service.StatsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserStatsService {
  private final UserService userService;
  private final StatsService statsService;

  public List<Stats> getAllStatsRecordsByUser(Long userId) {
    User user = userService.getUserById(userId);
    return statsService.getAllStatsRecordsByUser(user);
  }
}