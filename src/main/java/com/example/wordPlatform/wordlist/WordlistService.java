package com.example.wordPlatform.wordlist;

import com.example.wordPlatform.exception.notFound.WordlistNotFoundException;
import com.example.wordPlatform.user.UserEntity;
import com.example.wordPlatform.wordlist.dto.WordlistCreateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WordlistService {
  private final WordlistRepo wordlistRepo;

  public WordlistEntity getWordlistById(Long wordlistId) {
    return wordlistRepo.findById(wordlistId)
            .orElseThrow(WordlistNotFoundException::new);
  }

  public List<WordlistEntity> getAllWordlistsByUser(UserEntity user) {
    return wordlistRepo.findAllByUser(user);
  }

  public WordlistEntity createWordlist(
          UserEntity user,
          WordlistCreateDto dto
  ) {
    WordlistEntity newWordlist = new WordlistEntity(dto.title(), dto.description());
    newWordlist.setUser(user);

    return wordlistRepo.save(newWordlist);
  }

  public WordlistEntity save(WordlistEntity wordlist) {
    return wordlistRepo.save(wordlist);
  }
}
