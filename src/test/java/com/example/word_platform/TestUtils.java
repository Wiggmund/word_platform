package com.example.word_platform;

import static com.example.word_platform.TestConstants.ATTRIBUTE_BASE_TYPE;
import static com.example.word_platform.TestConstants.ATTRIBUTE_CUSTOM_TYPE;
import static com.example.word_platform.TestConstants.BASE_ATTRIBUTE_NAME_1;
import static com.example.word_platform.TestConstants.BASE_ATTRIBUTE_NAME_2;
import static com.example.word_platform.TestConstants.CUSTOM_ATTRIBUTE_NAME_1;
import static com.example.word_platform.TestConstants.CUSTOM_ATTRIBUTE_NAME_2;
import static com.example.word_platform.TestConstants.QUESTION_TEXT_1;
import static com.example.word_platform.TestConstants.QUESTION_TEXT_2;
import static com.example.word_platform.TestConstants.USER_EMAIL_1;
import static com.example.word_platform.TestConstants.USER_EMAIL_2;
import static com.example.word_platform.TestConstants.USER_USERNAME_1;
import static com.example.word_platform.TestConstants.USER_USERNAME_2;
import static com.example.word_platform.TestConstants.WORDLIST_DESCRIPTION_1;
import static com.example.word_platform.TestConstants.WORDLIST_DESCRIPTION_2;
import static com.example.word_platform.TestConstants.WORDLIST_TITLE_1;
import static com.example.word_platform.TestConstants.WORDLIST_TITLE_2;
import static com.example.word_platform.TestConstants.WORD_DEFINITION_1;
import static com.example.word_platform.TestConstants.WORD_DEFINITION_2;

import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import java.util.stream.Collectors;

public class TestUtils {
  public static AppUser getUser(TestDataVariant variant) {
    AppUser user1 = AppUser.builder()
        .username(USER_USERNAME_1)
        .email(USER_EMAIL_1)
        .build();

    AppUser user2 = AppUser.builder()
        .username(USER_USERNAME_2)
        .email(USER_EMAIL_2)
        .build();

    return switch (variant) {
      case FIRST -> user1;
      case SECOND -> user2;
    };
  }

  public static Wordlist getWordlist(TestDataVariant variant) {
    Wordlist wordlist1 = Wordlist.builder()
        .title(WORDLIST_TITLE_1)
        .description(WORDLIST_DESCRIPTION_1)
        .build();

    Wordlist wordlist2 = Wordlist.builder()
        .title(WORDLIST_TITLE_2)
        .description(WORDLIST_DESCRIPTION_2)
        .build();

    return switch (variant) {
      case FIRST -> wordlist1;
      case SECOND -> wordlist2;
    };
  }

  public static Attribute getBaseAttribute(TestDataVariant variant) {
    Attribute attribute1 = Attribute.builder()
        .name(BASE_ATTRIBUTE_NAME_1)
        .type(ATTRIBUTE_BASE_TYPE)
        .build();

    Attribute attribute2 = Attribute.builder()
        .name(BASE_ATTRIBUTE_NAME_2)
        .type(ATTRIBUTE_BASE_TYPE)
        .build();

    return switch (variant) {
      case FIRST -> attribute1;
      case SECOND -> attribute2;
    };
  }

  public static Attribute getCustomAttribute(TestDataVariant variant) {
    Attribute attribute1 = Attribute.builder()
        .name(CUSTOM_ATTRIBUTE_NAME_1)
        .type(ATTRIBUTE_CUSTOM_TYPE)
        .build();

    Attribute attribute2 = Attribute.builder()
        .name(CUSTOM_ATTRIBUTE_NAME_2)
        .type(ATTRIBUTE_CUSTOM_TYPE)
        .build();

    return switch (variant) {
      case FIRST -> attribute1;
      case SECOND -> attribute2;
    };
  }

  public static Question getQuestion(TestDataVariant variant, String type) {
    Question question1 = Question.builder()
        .text(QUESTION_TEXT_1)
        .type(type)
        .build();

    Question question2 = Question.builder()
        .text(QUESTION_TEXT_2)
        .type(type)
        .build();

    return switch (variant) {
      case FIRST -> question1;
      case SECOND -> question2;
    };
  }

  public static Word getWord(TestDataVariant variant) {
    Word word1 = Word.builder()
        .definition(WORD_DEFINITION_1)
        .build();

    Word word2 = Word.builder()
        .definition(WORD_DEFINITION_2)
        .build();

    return switch (variant) {
      case FIRST -> word1;
      case SECOND -> word2;
    };
  }

  public static String stringToRandomCase(String target) {
    if (target.isEmpty()) {
      throw new IllegalArgumentException("Passed empty string");
    }

    return target.chars().mapToObj(ch -> Character.isLowerCase(ch)
        ? String.valueOf(Character.toUpperCase((char) ch))
        : String.valueOf(Character.toLowerCase((char) ch))
    ).collect(Collectors.joining());
  }
}