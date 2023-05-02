package com.example.word_platform.service.user;

import com.example.word_platform.dto.wordlist.WordlistCreateDto;
import com.example.word_platform.dto.wordlist.WordlistUpdateDto;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.service.WordlistService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserWordlistService {
  private final UserService userService;
  private final WordlistService wordlistService;

  public List<Wordlist> getAllUserWordlists(Long userId) {
    User user = userService.getUserById(userId);
    return wordlistService.getAllWordlistsByUser(user);
  }

  public Wordlist getUserWordlistById(Long userId, Long wordlistId) {
    userService.getUserById(userId);
    return wordlistService.getWordlistById(wordlistId);
  }

  public Wordlist createUserWorldlist(Long userId, WordlistCreateDto dto) {
    User user = userService.getUserById(userId);

    Wordlist newWordlist = wordlistService.createWordlist(user, dto);
    user.addWordlist(newWordlist);
    userService.save(user);

    return newWordlist;
  }

  public Wordlist updateUserWordlist(Long userId, Long wordlistId, WordlistUpdateDto dto) {
    User user = userService.getUserById(userId);
    wordlistService.getWordlistByIdAndUser(wordlistId, user);
    return wordlistService.updateWordlist(wordlistId, dto);
  }

  public Wordlist removeUserWordlist(Long userId, Long wordlistId) {
    User user = userService.getUserById(userId);
    wordlistService.getWordlistByIdAndUser(wordlistId, user);
    return wordlistService.removeWordlistById(wordlistId);
  }
}
