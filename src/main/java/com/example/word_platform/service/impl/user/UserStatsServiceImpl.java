package com.example.word_platform.service.impl.user;

import com.example.word_platform.model.Stats;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.service.StatsService;
import com.example.word_platform.service.user.UserService;
import com.example.word_platform.service.user.UserStatsService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserStatsServiceImpl implements UserStatsService {
  private final UserService userService;
  private final StatsService statsService;

  public List<Stats> getAllStatsRecordsByUser(Long userId) {
    AppUser user = userService.getUserById(userId);
    return statsService.getAllStatsRecordsByUser(user);
  }

  public Stats removeStatsById(Long userId, Long statsId) {
    userService.getUserById(userId);
    return statsService.removeStatsById(statsId);
  }
}
