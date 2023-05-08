package com.example.word_platform.repository;

import static com.example.word_platform.TestConstants.NOT_EXISTING_ID_LONG;
import static com.example.word_platform.TestConstants.WORD_BASE_ATTRIBUTE_VALUE_1;
import static com.example.word_platform.TestConstants.WORD_BASE_ATTRIBUTE_VALUE_2;
import static com.example.word_platform.TestConstants.WORD_CUSTOM_ATTRIBUTE_VALUE_1;
import static com.example.word_platform.TestConstants.WORD_CUSTOM_ATTRIBUTE_VALUE_2;
import static com.example.word_platform.TestConstants.WORD_DEFINITION_1;
import static com.example.word_platform.TestConstants.WORD_DEFINITION_2;
import static com.example.word_platform.TestUtils.getBaseAttribute;
import static com.example.word_platform.TestUtils.getCustomAttribute;
import static com.example.word_platform.TestUtils.getUser;
import static com.example.word_platform.TestUtils.getWordlist;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.word_platform.TestDataVariant;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.model.word.WordsAttributes;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class WordRepoTest {
  private final AppUser user = getUser(TestDataVariant.FIRST);
  private final AppUser userWithoutWords = getUser(TestDataVariant.SECOND);
  private final Wordlist wordlist = getWordlist(TestDataVariant.FIRST);
  private final Wordlist wordlistWithoutWords = getWordlist(TestDataVariant.SECOND);
  private final Attribute attribute_1 = getBaseAttribute(TestDataVariant.FIRST);
  private final Attribute attribute_2 = getCustomAttribute(TestDataVariant.FIRST);
  private final Word word_1 = Word.builder()
      .definition(WORD_DEFINITION_1)
      .user(user)
      .wordlist(wordlist)
      .build();

  private final Word word_2 = Word.builder()
      .definition(WORD_DEFINITION_2)
      .user(user)
      .wordlist(wordlist)
      .build();
  private final List<Word> words = List.of(word_1, word_2);

  private final List<WordsAttributes> wordsAttributes_1 = List.of(
      WordsAttributes.builder()
          .word(word_1)
          .attribute(attribute_1)
          .text(WORD_BASE_ATTRIBUTE_VALUE_1)
          .build(),
      WordsAttributes.builder()
          .word(word_1)
          .attribute(attribute_2)
          .text(WORD_CUSTOM_ATTRIBUTE_VALUE_1)
          .build()
  );

  private final List<WordsAttributes> wordsAttributes_2 = List.of(
      WordsAttributes.builder()
          .word(word_2)
          .attribute(attribute_1)
          .text(WORD_BASE_ATTRIBUTE_VALUE_2)
          .build(),
      WordsAttributes.builder()
          .word(word_2)
          .attribute(attribute_2)
          .text(WORD_CUSTOM_ATTRIBUTE_VALUE_2)
          .build()
  );

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private WordRepo underTest;

  @BeforeEach
  void setUp() {
    entityManager.persist(user);
    entityManager.persist(userWithoutWords);
    entityManager.persist(wordlist);
    entityManager.persist(wordlistWithoutWords);

    entityManager.persist(attribute_1);
    entityManager.persist(attribute_2);

    word_1.setAttributes(wordsAttributes_1);
    word_2.setAttributes(wordsAttributes_2);

    entityManager.persist(word_1);
    entityManager.persist(word_2);
  }

  @Test
  void findAllByWordlist_ShouldReturnListOfWordlistWordsWithAttributeValues() {
    //given
    //when
    List<Word> actual = underTest.findAllByWordlistWithAttributes(wordlist);

    //then
    assertThat(actual.isEmpty()).isFalse();
    assertThat(actual.size()).isEqualTo(words.size());

    assertThat(actual)
        .withFailMessage("Wordlist of fetched words differs from expected")
        .allSatisfy(item -> assertThat(item.getWordlist()).isEqualTo(wordlist));

    assertThat(actual)
        .withFailMessage("Fetched words have empty attribute list")
        .allSatisfy(item -> assertThat(item.getAttributes().isEmpty()).isFalse());

    assertThat(actual)
        .withFailMessage("Fetched words differs from expected")
        .allSatisfy(item -> assertThat(item).isIn(words));
  }

  @Test
  void findAllByWordlistWithoutWords_ShouldReturnEmptyList() {
    //given
    //when
    List<Word> actual = underTest.findAllByWordlistWithAttributes(wordlistWithoutWords);

    //then
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  void findAllByUser_ShouldReturnListOfUserWords() {
    //given
    //when
    List<Word> actual = underTest.findAllByUser(user);

    //then
    assertThat(actual.isEmpty()).isFalse();
    assertThat(actual.size()).isEqualTo(words.size());

    assertThat(actual)
        .withFailMessage("User of fetched words differs from expected")
        .allSatisfy(item -> assertThat(item.getUser()).isEqualTo(user));

    assertThat(actual)
        .withFailMessage("Fetched words differs from expected")
        .allSatisfy(item -> assertThat(item).isIn(words));
  }

  @Test
  void findAllByUserWithoutWords_ShouldReturnEmptyList() {
    //given
    //when
    List<Word> actual = underTest.findAllByUser(userWithoutWords);

    //then
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  void findAllByExistingIdsAndWordlist_ShouldReturnListOfWordlistWords() {
    //given
    List<Long> ids = words.stream().map(Word::getId).toList();

    //when
    List<Word> actual = underTest.findAllByIdInAndWordlist(ids, wordlist);

    //then
    assertThat(actual.isEmpty()).isFalse();
    assertThat(actual.size()).isEqualTo(words.size());

    assertThat(actual)
        .withFailMessage("Wordlist of fetched words differs from expected")
        .allSatisfy(item -> assertThat(item.getWordlist()).isEqualTo(wordlist));

    assertThat(actual)
        .withFailMessage("Fetched word ids differs from expected")
        .allSatisfy(item -> assertThat(item.getId()).isIn(ids));
  }

  @Test
  void findAllByNotExistingIdsAndWordlist_ShouldReturnEmptyList() {
    //given
    List<Long> ids = List.of(NOT_EXISTING_ID_LONG);

    //when
    List<Word> actual = underTest.findAllByIdInAndWordlist(ids, wordlist);

    //then
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  void findByExistingWordlistAndDefinitionAndAttributeValues_ShouldReturnWordlistWord() {
    //given
    List<String> attributeValues =
        word_1.getAttributes().stream().map(WordsAttributes::getText).toList();

    //when
    Optional<Word> actual =
        underTest.findByWordlistAndDefinitionAndAttributeValues(
            WORD_DEFINITION_1,
            wordlist,
            attributeValues
        );

    //then
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getDefinition()).isEqualTo(WORD_DEFINITION_1);
    assertThat(actual.get().getWordlist()).isEqualTo(wordlist);

    assertThat(actual.get().getAttributes())
        .withFailMessage("Fetched attribute values differs from expected")
        .allSatisfy(item -> assertThat(item.getText()).isIn(attributeValues));
  }
}