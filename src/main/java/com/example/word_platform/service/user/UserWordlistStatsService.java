package com.example.word_platform.service.user;

import com.example.word_platform.dto.stats.StatsCreateDto;
import com.example.word_platform.model.Stats;
import java.util.List;

public interface UserWordlistStatsService {
  List<Stats> createStatsRecords(Long userId, Long wordlistId, List<StatsCreateDto> dto);
}
