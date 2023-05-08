package com.example.word_platform.repository;

import static com.example.word_platform.TestConstants.STATS_CORRECT_TRUE;
import static com.example.word_platform.TestUtils.getUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.word_platform.TestDataVariant;
import com.example.word_platform.model.Stats;
import com.example.word_platform.model.AppUser;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class StatsRepoTest {
  private final AppUser user = getUser(TestDataVariant.FIRST);
  private final AppUser userWithoutStatsRecords = getUser(TestDataVariant.SECOND);
  private final Stats stats_1 = Stats.builder()
      .testingDate(LocalDate.now())
      .correct(STATS_CORRECT_TRUE)
      .user(user)
      .build();

  private final Stats stats_2 = Stats.builder()
      .testingDate(LocalDate.now().plusDays(1))
      .correct(STATS_CORRECT_TRUE)
      .user(user)
      .build();

  private final List<Stats> statsRecords = List.of(stats_1, stats_2);
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private StatsRepo statsRepo;

  @BeforeEach
  void setUp() {
    entityManager.persist(user);
    entityManager.persist(userWithoutStatsRecords);
    entityManager.persist(stats_1);
    entityManager.persist(stats_2);
  }

  @Test
  void findAllByUser_ShouldReturnListOfUserStatsRecords() {
    //given
    //when
    List<Stats> actual = statsRepo.findAllByUser(user);

    //then
    assertThat(actual.isEmpty()).isFalse();
    assertThat(actual.size()).isEqualTo(statsRecords.size());

    assertThat(actual)
        .withFailMessage("Stats user differs from expected")
        .allSatisfy(item -> assertThat(item.getUser()).isEqualTo(user));

    assertThat(actual)
        .withFailMessage("Fetched stats records differs from expected")
        .allSatisfy(item -> assertThat(item).isIn(statsRecords));
  }

  @Test
  void findAllByUserWithoutStats_ShouldReturnEmptyList() {
    //given
    //when
    List<Stats> actual = statsRepo.findAllByUser(userWithoutStatsRecords);

    //then
    assertThat(actual.isEmpty()).isTrue();
  }
}