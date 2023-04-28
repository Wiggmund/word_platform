package com.example.word_platform.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.word_platform.model.Attribute;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AttributeRepoTest {
  private static final String UNEXISTED_ATTRIBUTE_NAME = "undefined";
  private static final String ATTRIBUTE_NAME_1 = "translation";
  private static final String ATTRIBUTE_NAME_1_RANDOM_CASE = "tRanSlaTioN";
  private static final String ATTRIBUTE_NAME_2 = "context";
  private static final String BASE_TYPE = "base";
  private static final String CUSTOM_TYPE = "custom";
  @Autowired
  private AttributeRepo underTest;

  @BeforeEach
  void setUp() {
    underTest.saveAll(
        List.of(
            Attribute.builder()
                .name(ATTRIBUTE_NAME_1)
                .type(BASE_TYPE)
                .build(),
            Attribute.builder()
                .name(ATTRIBUTE_NAME_2)
                .type(CUSTOM_TYPE)
                .build()
        )
    );
  }

  @AfterEach
  void tearDown() {
    underTest.deleteAll();
  }

  @Test
  void itShouldFindAttributeByName() {
    //when
    Optional<Attribute> expected = underTest.findByNameIgnoreCase(ATTRIBUTE_NAME_1);

    //then
    assertThat(expected.isEmpty()).isFalse();
  }

  @Test
  void itShouldNotFindAttributeByName() {
    //when
    Optional<Attribute> expected = underTest.findByNameIgnoreCase(UNEXISTED_ATTRIBUTE_NAME);

    //then
    assertThat(expected.isEmpty()).isTrue();
  }

  @Test
  void itShouldFindAttributeByNameIgnoringCase() {
    //when
    Optional<Attribute> expected = underTest.findByNameIgnoreCase(ATTRIBUTE_NAME_1_RANDOM_CASE);

    //then
    assertThat(expected.isPresent()).isTrue();
  }

  @Test
  void findAllByNameInShouldReturnTwoMatches() {
    //given
    List<String> attributeNames = List.of(ATTRIBUTE_NAME_1, ATTRIBUTE_NAME_2);

    //when
    List<Attribute> attributes = underTest.findAllByNameIgnoreCaseIn(attributeNames);

    //then
    assertThat(attributes.size()).isEqualTo(attributeNames.size());
  }

  @Test
  void findAllByNameInShouldReturnTwoMatchesIgnoringCase() {
    //given
    List<String> attributeNames = List.of(ATTRIBUTE_NAME_1_RANDOM_CASE, ATTRIBUTE_NAME_2);

    //when
    List<Attribute> attributes = underTest.findAllByNameIgnoreCaseIn(attributeNames);

    //then
    assertThat(attributes.size()).isEqualTo(attributeNames.size());
  }

  @Test
  void findAllByNameInShouldNotAnyAttributes() {
    //when
    List<Attribute> attributes = underTest.findAllByNameIgnoreCaseIn(List.of(UNEXISTED_ATTRIBUTE_NAME));

    //then
    assertThat(attributes.isEmpty()).isTrue();
  }
}