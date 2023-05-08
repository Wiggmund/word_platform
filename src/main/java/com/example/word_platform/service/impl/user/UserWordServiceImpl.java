package com.example.word_platform.service.impl.user;

import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.service.WordService;
import com.example.word_platform.service.user.UserService;
import com.example.word_platform.service.user.UserWordService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserWordServiceImpl implements UserWordService {
  private final UserService userService;
  private final WordService wordService;

  public List<Word> getAllUserWords(Long userId) {
    AppUser appUser = userService.getUserById(userId);
    return wordService.getAllWordsByUser(appUser);
  }
}
