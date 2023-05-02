package com.example.word_platform.controller.user;

import com.example.word_platform.dto.stats.StatsResponseDto;
import com.example.word_platform.model.Stats;
import com.example.word_platform.service.user.UserStatsService;
import com.example.word_platform.shared.EntityConverter;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/{userId}/stats")
@AllArgsConstructor
public class UserStatsController {
  private final UserStatsService userStatsService;
  private final EntityConverter entityConverter;

  @GetMapping
  public ResponseEntity<List<StatsResponseDto>> getAllStatsRecordsByUser(
      @PathVariable Long userId) {
    List<StatsResponseDto> fetchedRecords = userStatsService.getAllStatsRecordsByUser(userId)
        .stream()
        .map(entityConverter::entityToDto)
        .toList();

    return ResponseEntity.ok(fetchedRecords);
  }

  @DeleteMapping("{statsId}")
  public ResponseEntity<StatsResponseDto> removeStatsById(
      @PathVariable("userId") Long userId,
      @PathVariable("statsId") Long statsId
  ) {
    Stats removedStats = userStatsService.removeStatsById(userId, statsId);
    return ResponseEntity.ok(entityConverter.entityToDto(removedStats));
  }
}
