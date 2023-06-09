package com.example.word_platform.service.impl.user;

import com.example.word_platform.dto.wordlist.WordlistCreateDto;
import com.example.word_platform.dto.wordlist.WordlistUpdateDto;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.service.WordlistService;
import com.example.word_platform.service.user.UserService;
import com.example.word_platform.service.user.UserWordlistService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserWordlistServiceImpl implements UserWordlistService {
  private final UserService userService;
  private final WordlistService wordlistService;

  public List<Wordlist> getAllUserWordlists(Long userId) {
    AppUser user = userService.getUserById(userId);
    return wordlistService.getAllWordlistsByUser(user);
  }

  public Wordlist getUserWordlistById(Long userId, Long wordlistId) {
    userService.getUserById(userId);
    return wordlistService.getWordlistById(wordlistId);
  }

  public Wordlist createUserWorldlist(Long userId, WordlistCreateDto dto) {
    AppUser user = userService.getUserById(userId);

    Wordlist newWordlist = wordlistService.createWordlist(user, dto);
    user.addWordlist(newWordlist);
    userService.save(user);

    return newWordlist;
  }

  public Wordlist updateUserWordlist(Long userId, Long wordlistId, WordlistUpdateDto dto) {
    AppUser user = userService.getUserById(userId);
    wordlistService.getWordlistByIdAndUser(wordlistId, user);
    return wordlistService.updateWordlist(wordlistId, dto);
  }

  public Wordlist removeUserWordlist(Long userId, Long wordlistId) {
    AppUser user = userService.getUserById(userId);
    wordlistService.getWordlistByIdAndUser(wordlistId, user);
    return wordlistService.removeWordlistById(wordlistId);
  }
}
