package com.example.word_platform.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.word_platform.model.Attribute;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AttributeRepoTest {
  @Autowired
  private AttributeRepo underTest;

  @BeforeEach
  void setUp() {
    underTest.saveAllAndFlush(
        List.of(
            Attribute.builder()
                .name("translation")
                .type("base")
                .build(),
            Attribute.builder()
                .name("context")
                .type("custom")
                .build()
        )
    );
  }

  @Test
  void itShouldFindAttributeByName() {
    //when
    Optional<Attribute> expected = underTest.findByName("translation");

    //then
    assertThat(expected.isEmpty()).isFalse();
  }

  @Test
  void itShouldNotFindAttributeByName() {
    //when
    Optional<Attribute> expected = underTest.findByName("undefined");

    //then
    assertThat(expected.isEmpty()).isTrue();
  }

  @Test
  void findAllByNameInShouldReturnTwoMatches() {
    //when
    List<Attribute> attributes = underTest.findAllByNameIn(List.of("translation", "context"));

    //then
    assertThat(attributes.size()).isEqualTo(2);
  }

  @Test
  void findAllByNameInShouldNotAnyAttributes() {
    //when
    List<Attribute> attributes = underTest.findAllByNameIn(List.of("undefined1", "undefined2"));

    //then
    assertThat(attributes.size()).isEqualTo(0);
  }
}