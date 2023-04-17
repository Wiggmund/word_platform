package com.example.wordPlatform.word.service;

import com.example.wordPlatform.attribute.AttributeEntity;
import com.example.wordPlatform.shared.DuplicationCheckService;
import com.example.wordPlatform.user.UserEntity;
import com.example.wordPlatform.word.WordEntity;
import com.example.wordPlatform.word.WordRepo;
import com.example.wordPlatform.word.WordsAttributesEntity;
import com.example.wordPlatform.word.dto.WordCreateDto;
import com.example.wordPlatform.wordlist.WordlistEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class WordService {
  private final WordRepo wordRepo;
  private final DuplicationCheckService duplicationCheckService;

  public WordEntity createWord(
          UserEntity user,
          WordlistEntity wordlist,
          WordCreateDto dto,
          Map<AttributeEntity, String> wordAttributes
  ) {
    duplicationCheckService.checkWordForValueAndList(dto.value(), wordlist);

    WordEntity newWord = new WordEntity(dto.value());
    newWord.setUser(user);
    newWord.setWordlist(wordlist);

    List<WordsAttributesEntity> attributes = wordAttributes.entrySet().stream()
            .map(item -> newWord.addAttribute(item.getKey(), item.getValue()))
            .toList();
    newWord.setAttributes(attributes);

    return wordRepo.save(newWord);
  }

  public List<WordEntity> getAllWordsByListWithAttributes(WordlistEntity wordlist) {
    return wordRepo.findAllByListWithAttributes(wordlist);
  }
}
