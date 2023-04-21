package com.example.word_platform.user.service;

import com.example.word_platform.user.UserEntity;
import com.example.word_platform.word.WordEntity;
import com.example.word_platform.word.service.WordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserWordService {
  private final UserService userService;
  private final WordService wordService;

  public List<WordEntity> getAllUserWords(Long userId) {
    UserEntity user = userService.getUserById(userId);
    return wordService.getAllWordsByUser(user);
  }
}
