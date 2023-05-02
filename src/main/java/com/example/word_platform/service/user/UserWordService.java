package com.example.word_platform.service.user;

import com.example.word_platform.model.word.Word;
import java.util.List;

public interface UserWordService {
  List<Word> getAllUserWords(Long userId);
}