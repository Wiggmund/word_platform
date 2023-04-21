package com.example.word_platform.user.service;

import com.example.word_platform.user.UserEntity;
import com.example.word_platform.wordlist.dto.WordlistCreateDto;
import com.example.word_platform.wordlist.WordlistEntity;
import com.example.word_platform.wordlist.WordlistService;
import com.example.word_platform.wordlist.dto.WordlistUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserWordlistService {
  private final UserService userService;
  private final WordlistService wordlistService;
  public List<WordlistEntity> getAllUserWordlists(Long userId) {
    UserEntity user = userService.getUserById(userId);
    return wordlistService.getAllWordlistsByUser(user);
  }

  public WordlistEntity createUserWorldlist(Long userId, WordlistCreateDto dto) {
    UserEntity user = userService.getUserById(userId);

    WordlistEntity newWordlist =  wordlistService.createWordlist(user, dto);
    user.addWordlist(newWordlist);
    userService.save(user);

    return newWordlist;
  }

  public WordlistEntity getUserWordlistById(Long userId, Long wordlistId) {
    userService.getUserById(userId);
    return wordlistService.getWordlistById(wordlistId);
  }

  public WordlistEntity updateUserWordlist(Long userId, Long wordlistId, WordlistUpdateDto dto) {
    userService.getUserById(userId);
    return wordlistService.updateWordlist(wordlistId, dto);
  }
}
