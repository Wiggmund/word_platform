package com.example.word_platform.repository;

import static com.example.word_platform.TestConstants.BASE_ATTRIBUTE_NAME_1;
import static com.example.word_platform.TestConstants.CUSTOM_ATTRIBUTE_NAME_1;
import static com.example.word_platform.TestConstants.NOT_EXISTING_VALUE_STRING;
import static com.example.word_platform.TestUtils.getBaseAttribute;
import static com.example.word_platform.TestUtils.getCustomAttribute;
import static com.example.word_platform.TestUtils.stringToRandomCase;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.word_platform.TestDataVariant;
import com.example.word_platform.model.Attribute;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class AttributeRepoTest {
  private static final String BASE_ATTRIBUTE_NAME_1_RANDOM_CASE =
      stringToRandomCase(BASE_ATTRIBUTE_NAME_1);

  private final Attribute baseAttribute = getBaseAttribute(TestDataVariant.FIRST);
  private final Attribute customAttribute = getCustomAttribute(TestDataVariant.FIRST);

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AttributeRepo underTest;

  @BeforeEach
  public void setUp() {
    entityManager.persist(baseAttribute);
    entityManager.persist(customAttribute);
  }

  @Test
  void findAttributeByName_ShouldReturnOptionalOfAttribute() {
    //given
    //when
    Optional<Attribute> actual = underTest.findByNameIgnoreCase(BASE_ATTRIBUTE_NAME_1);

    //then
    assertThat(actual).isInstanceOf(Optional.class);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getName()).isEqualTo(BASE_ATTRIBUTE_NAME_1);
  }

  @Test
  void findAttributeByNameInRandomCase_ShouldReturnOptionalOfAttribute() {
    //given
    //when
    Optional<Attribute> actual = underTest.findByNameIgnoreCase(BASE_ATTRIBUTE_NAME_1_RANDOM_CASE);

    //then
    assertThat(actual).isInstanceOf(Optional.class);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get().getName()).isEqualTo(BASE_ATTRIBUTE_NAME_1);
  }

  @Test
  void findAttributeByNotExistingName_ShouldReturnEmptyOptional() {
    //given
    //when
    Optional<Attribute> actual = underTest.findByNameIgnoreCase(NOT_EXISTING_VALUE_STRING);

    //then
    assertThat(actual).isInstanceOf(Optional.class);
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  void findAllAttributesByNames_ShouldReturnListOfAttributesForProvidedNames() {
    //given
    List<String> attributeNames = List.of(BASE_ATTRIBUTE_NAME_1, CUSTOM_ATTRIBUTE_NAME_1);

    //when
    List<Attribute> actual = underTest.findAllByNameIgnoreCaseIn(attributeNames);

    //then
    assertThat(actual.size()).isEqualTo(attributeNames.size());
    assertThat(actual)
        .withFailMessage("Fetched attribute names differs from expected")
        .allSatisfy(item -> assertThat(item.getName()).isIn(attributeNames));
  }

  @Test
  void findAllAttributesByNamesWithRandomCase_ShouldReturnListOfAttributesForProvidedNames() {
    //given
    List<String> attributeNames = List.of(BASE_ATTRIBUTE_NAME_1, CUSTOM_ATTRIBUTE_NAME_1);

    //when
    List<Attribute> actual = underTest.findAllByNameIgnoreCaseIn(List.of(
        BASE_ATTRIBUTE_NAME_1_RANDOM_CASE, CUSTOM_ATTRIBUTE_NAME_1
    ));

    //then
    assertThat(actual.size()).isEqualTo(attributeNames.size());
    assertThat(actual)
        .withFailMessage("Fetched attribute names differs from expected")
        .allSatisfy(item -> assertThat(item.getName()).isIn(attributeNames));
  }

  @Test
  void findAllAttributesByNotExistingNames_ShouldReturnEmptyList() {
    //given
    //when
    List<Attribute> attributes = underTest.findAllByNameIgnoreCaseIn(List.of(
        NOT_EXISTING_VALUE_STRING));

    //then
    assertThat(attributes.isEmpty()).isTrue();
  }
}