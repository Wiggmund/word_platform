package com.example.word_platform;

import com.example.word_platform.enums.AttributeType;

public class TestConstants {
  //COMMON
  public static final String NOT_EXISTING_VALUE_STRING = "undefined";
  public static final Long EXISTING_ID_LONG = 1L;
  public static final Long NOT_EXISTING_ID_LONG = 999L;

  //ATTRIBUTE
  public static final String BASE_ATTRIBUTE_NAME_1 = "translation";
  public static final String BASE_ATTRIBUTE_NAME_2 = "transcription";
  public static final String CUSTOM_ATTRIBUTE_NAME_1 = "context";
  public static final String CUSTOM_ATTRIBUTE_NAME_2 = "song example";
  public static final AttributeType ATTRIBUTE_BASE_TYPE = AttributeType.base;
  public static final AttributeType ATTRIBUTE_CUSTOM_TYPE = AttributeType.custom;

  //USER
  public static final String USER_USERNAME_1 = "Sem";
  public static final String USER_USERNAME_2 = "Mark";
  public static final String USER_EMAIL_1 = "semworking@gmail.com";
  public static final String USER_EMAIL_2 = "markworking@gmail.com";

  //WORDLIST
  public static final String WORDLIST_TITLE_1 = "Top 100 verbs";
  public static final String WORDLIST_TITLE_2 = "Top 100 nouns";
  public static final String WORDLIST_DESCRIPTION_1 = "The most popular verbs";
  public static final String WORDLIST_DESCRIPTION_2 = "The most popular nouns";

  //WORD
  public static final String WORD_DEFINITION_1 = "weekend";
  public static final String WORD_DEFINITION_2 = "room";
  public static final String WORD_BASE_ATTRIBUTE_VALUE_1 = "free time";
  public static final String WORD_BASE_ATTRIBUTE_VALUE_2 = "some space in building";
  public static final String WORD_CUSTOM_ATTRIBUTE_VALUE_1 = "I am enjoying my weekends";
  public static final String WORD_CUSTOM_ATTRIBUTE_VALUE_2 = "Do you have free room?";

  //QUESTION
  public static final String QUESTION_TEXT_1 = "Translate it";
  public static final String QUESTION_TEXT_2 = "Provide some examples";
  public static final String QUESTION_TYPE_CHECKED = "checked";
  public static final String QUESTION_TYPE_NOT_CHECKED = "unchecked";

  //STATS
  public static final String STATS_TESTING_DATE_1 = "2023-04-29";
  public static final String STATS_TESTING_DATE_2 = "2023-04-27";
  public static final Boolean STATS_CORRECT_TRUE = true;
  public static final Boolean STATS_CORRECT_FALSE = false;
}
