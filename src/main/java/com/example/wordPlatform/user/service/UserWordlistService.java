package com.example.wordPlatform.user.service;

import com.example.wordPlatform.user.UserEntity;
import com.example.wordPlatform.wordlist.dto.WordlistCreateDto;
import com.example.wordPlatform.wordlist.WordlistEntity;
import com.example.wordPlatform.wordlist.WordlistService;
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
}
