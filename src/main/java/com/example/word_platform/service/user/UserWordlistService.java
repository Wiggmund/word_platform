package com.example.word_platform.service.user;

import com.example.word_platform.dto.wordlist.WordlistCreateDto;
import com.example.word_platform.dto.wordlist.WordlistUpdateDto;
import com.example.word_platform.model.Wordlist;
import java.util.List;

public interface UserWordlistService {
  List<Wordlist> getAllUserWordlists(Long userId);

  Wordlist getUserWordlistById(Long userId, Long wordlistId);

  Wordlist createUserWorldlist(Long userId, WordlistCreateDto dto);

  Wordlist updateUserWordlist(Long userId, Long wordlistId, WordlistUpdateDto dto);

  Wordlist removeUserWordlist(Long userId, Long wordlistId);
}
