package com.example.word_platform.repository;

import static com.example.word_platform.TestConstants.WORD_BASE_ATTRIBUTE_VALUE_1;
import static com.example.word_platform.TestConstants.WORD_CUSTOM_ATTRIBUTE_VALUE_1;
import static com.example.word_platform.TestUtils.getBaseAttribute;
import static com.example.word_platform.TestUtils.getCustomAttribute;
import static com.example.word_platform.TestUtils.getWord;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.word_platform.TestDataVariant;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.model.word.WordsAttributes;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class WordsAttributesRepoTest {
  private final Word word = getWord(TestDataVariant.FIRST);
  private final Word wordWithoutAttributes = getWord(TestDataVariant.SECOND);
  private final Attribute baseAttribute = getBaseAttribute(TestDataVariant.FIRST);
  private final Attribute customAttribute = getCustomAttribute(TestDataVariant.FIRST);
  private final WordsAttributes wordAttribute_1 = WordsAttributes.builder()
      .word(word)
      .attribute(baseAttribute)
      .text(WORD_BASE_ATTRIBUTE_VALUE_1)
      .build();
  private final WordsAttributes wordAttribute_2 = WordsAttributes.builder()
      .word(word)
      .attribute(customAttribute)
      .text(WORD_CUSTOM_ATTRIBUTE_VALUE_1)
      .build();
  private final List<WordsAttributes> wordsAttributes = List.of(wordAttribute_1, wordAttribute_2);
  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private WordsAttributesRepo underTest;

  @BeforeEach
  void setUp() {
    entityManager.persist(word);
    entityManager.persist(wordWithoutAttributes);

    entityManager.persist(baseAttribute);
    entityManager.persist(customAttribute);

    entityManager.persist(wordAttribute_1);
    entityManager.persist(wordAttribute_2);
  }

  @Test
  void findAllByWord_ShouldReturnListOfWordsAttributesEntriesForWord() {
    //given
    //when
    List<WordsAttributes> actual = underTest.findAllByWord(word);

    //then
    assertThat(actual.isEmpty()).isFalse();
    assertThat(actual.size()).isEqualTo(wordsAttributes.size());

    assertThat(actual)
        .withFailMessage("Word of fetched wordsAttributes entries differs from expected")
        .allSatisfy(item -> assertThat(item.getWord()).isEqualTo(word));

    assertThat(actual)
        .withFailMessage("WordsAttributes entries differ from expected")
        .allSatisfy(item -> assertThat(item).isIn(wordsAttributes));
  }

  @Test
  void findAllByWordWithoutAttributes_ShouldReturnEmptyListOfWordsAttributesEntriesForWord() {
    //given
    //when
    List<WordsAttributes> actual = underTest.findAllByWord(wordWithoutAttributes);

    //then
    assertThat(actual.isEmpty()).isTrue();
  }
}