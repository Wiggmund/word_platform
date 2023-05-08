package com.example.word_platform.repository;

import static com.example.word_platform.TestConstants.EXISTING_ID_LONG;
import static com.example.word_platform.TestConstants.NOT_EXISTING_ID_LONG;
import static com.example.word_platform.TestConstants.NOT_EXISTING_VALUE_STRING;
import static com.example.word_platform.TestConstants.WORDLIST_TITLE_1;
import static com.example.word_platform.TestUtils.getUser;
import static com.example.word_platform.TestUtils.getWordlist;
import static com.example.word_platform.TestUtils.stringToRandomCase;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.word_platform.TestDataVariant;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WordlistRepoTest {
  private final String WORDLIST_TITLE_1_RANDOM_CASE = stringToRandomCase(WORDLIST_TITLE_1);
  private final AppUser appUser = getUser(TestDataVariant.FIRST);
  private final AppUser appUserWithoutWordlists = getUser(TestDataVariant.SECOND);
  private final Wordlist wordlist_1 = getWordlist(TestDataVariant.FIRST);
  private final Wordlist wordlist_2 = getWordlist(TestDataVariant.SECOND);
  private final List<Wordlist> wordlists = List.of(wordlist_1, wordlist_2);
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private WordlistRepo underTest;

  @BeforeEach
  public void setUp() {
    wordlists.forEach(item -> {
      item.setAppUser(appUser);
      entityManager.persist(item);
    });

    appUser.setWordlists(wordlists);
    entityManager.persist(appUser);
    entityManager.persist(appUserWithoutWordlists);
  }

  @Test
  public void findAllByUser_ShouldReturnListOfUserWordlists() {
    //given
    //when
    List<Wordlist> actual = underTest.findAllByUser(appUser);

    //then
    assertThat(actual.isEmpty()).isFalse();
    assertThat(actual.size()).isEqualTo(wordlists.size());

    assertThat(actual)
        .withFailMessage("User of fetched wordlists differs from expected")
        .allSatisfy(item -> assertThat(item.getAppUser()).isEqualTo(appUser));

    assertThat(actual)
        .withFailMessage("Wordlists differ from expected")
        .allSatisfy(item -> assertThat(item).isIn(wordlists));
  }

  @Test
  public void findAllByUserWithoutWordlists_ShouldReturnEmptyList() {
    //given
    //when
    List<Wordlist> actual = underTest.findAllByUser(appUserWithoutWordlists);

    //then
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  public void findByIdAndUser_ShouldReturnOptionalOfWordlistForSpecifiedIdAndUser() {
    //given
    //when
    Optional<Wordlist> actual =
        underTest.findByIdAndUser(EXISTING_ID_LONG, appUser);
    System.out.println(underTest.findAll());

    //then
    assertThat(actual).isInstanceOf(Optional.class);
    assertThat(actual.isPresent()).isTrue();

    assertThat(actual.get().getId())
        .withFailMessage("Fetched wordlist id differs from expected")
        .isEqualTo(EXISTING_ID_LONG);

    assertThat(actual.get().getAppUser())
        .withFailMessage("Fetched wordlist user differs from expected")
        .isEqualTo(appUser);
  }

  @Test
  public void findByNotExistingIdAndUser_ShouldReturnEmptyOptional() {
    //given
    //when
    Optional<Wordlist> actual =
        underTest.findByIdAndUser(NOT_EXISTING_ID_LONG, appUser);

    //then
    assertThat(actual).isInstanceOf(Optional.class);
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  public void findByIdAndUserWithoutWordlists_ShouldReturnEmptyOptional() {
    //given
    //when
    Optional<Wordlist> actual =
        underTest.findByIdAndUser(EXISTING_ID_LONG, appUserWithoutWordlists);

    //then
    assertThat(actual).isInstanceOf(Optional.class);
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  public void findByTitle_ShouldReturnOptionalOfWordlist() {
    //given
    //when
    Optional<Wordlist> actual = underTest.findByTitleIgnoreCase(WORDLIST_TITLE_1);

    //then
    assertThat(actual).isInstanceOf(Optional.class);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getTitle()).isEqualTo(WORDLIST_TITLE_1);
  }

  @Test
  public void findByTitleIgnoreCase_ShouldReturnOptionalOfWordlist() {
    //given
    //when
    Optional<Wordlist> actual = underTest.findByTitleIgnoreCase(WORDLIST_TITLE_1_RANDOM_CASE);

    //then
    assertThat(actual).isInstanceOf(Optional.class);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getTitle()).isEqualTo(WORDLIST_TITLE_1);
  }

  @Test
  public void findByNotExistingTitle_ShouldReturnEmptyOptional() {
    //given
    //when
    Optional<Wordlist> actual = underTest.findByTitleIgnoreCase(NOT_EXISTING_VALUE_STRING);

    //then
    assertThat(actual).isInstanceOf(Optional.class);
    assertThat(actual.isPresent()).isFalse();
  }
}