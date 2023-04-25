package com.example.word_platform.service.user;

import com.example.word_platform.model.User;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.service.WordService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserWordService {
  private final UserService userService;
  private final WordService wordService;

  public List<Word> getAllUserWords(Long userId) {
    User user = userService.getUserById(userId);
    return wordService.getAllWordsByUser(user);
  }
}
