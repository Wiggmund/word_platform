package com.example.word_platform.repository;

import static com.example.word_platform.TestConstants.ATTRIBUTE_BASE_TYPE;
import static com.example.word_platform.TestConstants.NOT_EXISTING_ID_LONG;
import static com.example.word_platform.TestConstants.NOT_EXISTING_VALUE_STRING;
import static com.example.word_platform.TestConstants.QUESTION_TEXT_1;
import static com.example.word_platform.TestConstants.QUESTION_TEXT_2;
import static com.example.word_platform.TestConstants.QUESTION_TYPE_CHECKED;
import static com.example.word_platform.TestUtils.getBaseAttribute;
import static com.example.word_platform.TestUtils.getUser;
import static com.example.word_platform.TestUtils.getWordlist;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.word_platform.TestDataVariant;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class QuestionRepoTest {
  private final AppUser user = getUser(TestDataVariant.FIRST);
  private final Wordlist wordlist = getWordlist(TestDataVariant.FIRST);
  private final Attribute attribute_1 = getBaseAttribute(TestDataVariant.FIRST);
  private final Attribute attribute_2 = getBaseAttribute(TestDataVariant.SECOND);
  private final AppUser userWithoutQuestions = getUser(TestDataVariant.SECOND);
  private final Wordlist wordlistWithoutQuestions = getWordlist(TestDataVariant.SECOND);
  private final Attribute notRelatedAttribute = Attribute.builder()
      .name(NOT_EXISTING_VALUE_STRING)
      .type(ATTRIBUTE_BASE_TYPE)
      .build();
  private final Question question_1 = Question.builder()
      .text(QUESTION_TEXT_1)
      .type(QUESTION_TYPE_CHECKED)
      .user(user)
      .wordlist(wordlist)
      .answer(attribute_1)
      .build();

  private final Question question_2 = Question.builder()
      .text(QUESTION_TEXT_2)
      .type(QUESTION_TYPE_CHECKED)
      .user(user)
      .wordlist(wordlist)
      .answer(attribute_2)
      .build();

  @Autowired
  private QuestionRepo underTest;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void setUp() {
    entityManager.persist(user);
    entityManager.persist(wordlist);
    entityManager.persist(attribute_1);
    entityManager.persist(attribute_2);

    entityManager.persist(userWithoutQuestions);
    entityManager.persist(wordlistWithoutQuestions);
    entityManager.persist(notRelatedAttribute);
  }

  @Test
  void findByUserAndWordlistAndAnswer_ShouldReturnOptionalOfQuestion() {
    //given
    underTest.save(question_1);

    //when
    Optional<Question> actual =
        underTest.findByUserAndWordlistAndAnswer(user, wordlist, attribute_1);

    //then
    assertThat(actual.isPresent()).isTrue();

    assertThat(actual.get().getUser())
        .withFailMessage("User differs from expected")
        .isEqualTo(user);

    assertThat(actual.get().getWordlist())
        .withFailMessage("Wordlist differs from expected")
        .isEqualTo(wordlist);

    assertThat(actual.get().getAnswer())
        .withFailMessage("Answer differs from expected")
        .isEqualTo(attribute_1);
  }

  @Test
  void findByUserWithoutQuestionsAndWordlistAndAnswer_ShouldReturnEmptyOptional() {
    //given
    underTest.save(question_1);

    //when
    Optional<Question> actual =
        underTest.findByUserAndWordlistAndAnswer(userWithoutQuestions, wordlist, attribute_1);

    //then
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  void findByUserAndWordlistWithoutQuestionsAndAnswer_ShouldReturnEmptyOptional() {
    //given
    underTest.save(question_1);

    //when
    Optional<Question> actual =
        underTest.findByUserAndWordlistAndAnswer(user, wordlistWithoutQuestions, attribute_1);

    //then
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  void findByUserAndWordlistAndNotRelatedAnswer_ShouldReturnEmptyOptional() {
    //given
    underTest.save(question_1);

    //when
    Optional<Question> actual =
        underTest.findByUserAndWordlistAndAnswer(user, wordlist, notRelatedAttribute);

    //then
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  void findAllByWordlist_ShouldReturnListOfWordlistQuestions() {
    //given
    List<Question> questions = List.of(question_1, question_2);
    underTest.saveAll(questions);

    //when
    List<Question> actual = underTest.findAllByWordlist(wordlist);

    //then
    assertThat(actual.isEmpty()).isFalse();
    assertThat(actual.size()).isEqualTo(questions.size());
    assertThat(actual)
        .withFailMessage("Wordlist differs from expected")
        .allSatisfy(item -> assertThat(item.getWordlist()).isEqualTo(wordlist));
  }

  @Test
  void findAllByWordlistWithoutQuestions_ShouldReturnEmptyList() {
    //given
    List<Question> questions = List.of(question_1, question_2);
    underTest.saveAll(questions);

    //when
    List<Question> actual = underTest.findAllByWordlist(wordlistWithoutQuestions);

    //then
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  void findAllByIdsAndWordlist_ShouldReturnListOfWordlistQuestionsWithProvidedIds() {
    //given
    List<Question> questions = underTest.saveAll(
        List.of(question_1, question_2)
    );
    List<Long> ids = questions.stream().map(Question::getId).toList();

    //when
    List<Question> actual = underTest.findAllByIdInAndWordlist(ids, wordlist);

    //then
    assertThat(actual.isEmpty()).isFalse();
    assertThat(actual.size()).isEqualTo(questions.size());

    assertThat(actual)
        .withFailMessage("Fetched questions differs from expected")
        .allSatisfy(item -> assertThat(item).isIn(questions));

    assertThat(actual)
        .withFailMessage("Fetched question ids differs from expected")
        .allSatisfy(item -> assertThat(item.getId()).isIn(ids));
  }

  @Test
  void findAllByNotExistingIdsAndWordlist_ShouldReturnEmptyList() {
    //given
    List<Question> questions = underTest.saveAll(
        List.of(question_1, question_2)
    );
    List<Long> ids = List.of(NOT_EXISTING_ID_LONG);

    //when
    List<Question> actual = underTest.findAllByIdInAndWordlist(ids, wordlist);

    //then
    assertThat(actual.isEmpty()).isTrue();
  }
}