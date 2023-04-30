package com.example.word_platform.repository;

import static com.example.word_platform.TestConstants.NOT_EXISTING_VALUE_STRING;
import static com.example.word_platform.TestConstants.USER_EMAIL_1;
import static com.example.word_platform.TestConstants.USER_USERNAME_1;
import static com.example.word_platform.TestUtils.getUser;
import static com.example.word_platform.TestUtils.stringToRandomCase;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.word_platform.TestDataVariant;
import com.example.word_platform.model.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserRepoTest {
  private final User user = getUser(TestDataVariant.FIRST);
  private final String USER_USERNAME_1_RANDOM_CASE = stringToRandomCase(USER_USERNAME_1);

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepo underTest;


  @BeforeEach
  void setUp() {
    entityManager.persist(user);
  }

  @Test
  void findByUsernameOrEmail_ShouldReturnOptionalOfUser() {
    //given
    //when
    Optional<User> actual =
        underTest.findByUsernameIgnoreCaseOrEmail(USER_USERNAME_1, USER_EMAIL_1);

    //then
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByUsernameIgnoreCaseOrNotExistingEmail_ShouldReturnOptionalOfUser() {
    //given
    //when
    Optional<User> actual =
        underTest.findByUsernameIgnoreCaseOrEmail(USER_USERNAME_1_RANDOM_CASE,
            NOT_EXISTING_VALUE_STRING);

    //then
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByNotExistingUsernameOrEmail_ShouldReturnOptionalOfUserWithSpecifiedEmail() {
    //given
    //when
    Optional<User> actual =
        underTest.findByUsernameIgnoreCaseOrEmail(NOT_EXISTING_VALUE_STRING, USER_EMAIL_1);

    //then
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByUsernameOrNotExistingEmail_ShouldReturnOptionalOfUserWithSpecifiedUsername() {
    //given
    //when
    Optional<User> actual =
        underTest.findByUsernameIgnoreCaseOrEmail(USER_USERNAME_1, NOT_EXISTING_VALUE_STRING);

    //then
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByNotExistingUsernameOrNotExistingEmail_ShouldReturnEmptyOptional() {
    //given
    //when
    Optional<User> actual =
        underTest.findByUsernameIgnoreCaseOrEmail(NOT_EXISTING_VALUE_STRING,
            NOT_EXISTING_VALUE_STRING);

    //then
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  void findByUsername_ShouldReturnOptionalOfUser() {
    //given
    //when
    Optional<User> actual =
        underTest.findByUsernameIgnoreCase(USER_USERNAME_1);

    //then
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByNotExistingUsername_ShouldReturnEmptyOptional() {
    //given
    //when
    Optional<User> actual =
        underTest.findByUsernameIgnoreCase(NOT_EXISTING_VALUE_STRING);

    //then
    assertThat(actual.isPresent()).isFalse();
  }

  @Test
  void findByUsernameIgnoreCase_ShouldReturnOptionalOfUser() {
    //given
    //when
    Optional<User> actual =
        underTest.findByUsernameIgnoreCase(USER_USERNAME_1_RANDOM_CASE);

    //then
    System.out.println(user.getUsername());
    System.out.println(USER_USERNAME_1_RANDOM_CASE);
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }


  @Test
  void findByEmail_ShouldReturnOptionalOfUser() {
    //given
    //when
    Optional<User> actual =
        underTest.findByEmail(USER_EMAIL_1);

    //then
    assertThat(actual.isPresent()).isTrue();
    assertThat(actual.get()).isEqualTo(user);
  }

  @Test
  void findByNotExistingEmail_ShouldReturnEmptyOptional() {
    //given
    //when
    Optional<User> actual =
        underTest.findByEmail(NOT_EXISTING_VALUE_STRING);

    //then
    assertThat(actual.isPresent()).isFalse();
  }
}