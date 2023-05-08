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
    AppUser appUser = userService.getUserById(userId);
    return wordlistService.getAllWordlistsByUser(appUser);
  }

  public Wordlist getUserWordlistById(Long userId, Long wordlistId) {
    userService.getUserById(userId);
    return wordlistService.getWordlistById(wordlistId);
  }

  public Wordlist createUserWorldlist(Long userId, WordlistCreateDto dto) {
    AppUser appUser = userService.getUserById(userId);

    Wordlist newWordlist = wordlistService.createWordlist(appUser, dto);
    appUser.addWordlist(newWordlist);
    userService.save(appUser);

    return newWordlist;
  }

  public Wordlist updateUserWordlist(Long userId, Long wordlistId, WordlistUpdateDto dto) {
    AppUser appUser = userService.getUserById(userId);
    wordlistService.getWordlistByIdAndUser(wordlistId, appUser);
    return wordlistService.updateWordlist(wordlistId, dto);
  }

  public Wordlist removeUserWordlist(Long userId, Long wordlistId) {
    AppUser appUser = userService.getUserById(userId);
    wordlistService.getWordlistByIdAndUser(wordlistId, appUser);
    return wordlistService.removeWordlistById(wordlistId);
  }
}
