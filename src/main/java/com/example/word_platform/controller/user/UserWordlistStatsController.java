package com.example.word_platform.controller.user;

import com.example.word_platform.dto.stats.StatsCreateDto;
import com.example.word_platform.dto.stats.StatsResponseDto;
import com.example.word_platform.service.user.UserWordlistStatsService;
import com.example.word_platform.shared.EntityConverter;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/{userId}/wordlists/{wordlistId}/stats")
@AllArgsConstructor
public class UserWordlistStatsController {
  private final UserWordlistStatsService userWordlistStatsService;
  private final EntityConverter entityConverter;

  @PostMapping
  public ResponseEntity<List<StatsResponseDto>> createStatsRecords(
      @PathVariable Long userId,
      @PathVariable Long wordlistId,
      @RequestBody List<StatsCreateDto> dto
  ) {
    List<StatsResponseDto> createdRecords =
        userWordlistStatsService.createStatsRecords(userId, wordlistId, dto)
            .stream()
            .map(entityConverter::entityToDto)
            .toList();

    return ResponseEntity.status(HttpStatus.CREATED).body(createdRecords);
  }
}
