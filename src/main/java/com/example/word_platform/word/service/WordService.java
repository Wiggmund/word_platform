package com.example.word_platform.word.service;

import com.example.word_platform.attribute.dto.AttributeWithValuesDto;
import com.example.word_platform.exception.not_found.WordNotFoundException;
import com.example.word_platform.user.UserEntity;
import com.example.word_platform.word.WordEntity;
import com.example.word_platform.word.WordRepo;
import com.example.word_platform.word.dto.WordCreateDto;
import com.example.word_platform.word.dto.WordUpdateDto;
import com.example.word_platform.wordlist.WordlistEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WordService {
  private final WordRepo wordRepo;
  private final WordsAttributesService wordsAttributesService;

  public List<WordEntity> getAllWords() {
    return wordRepo.findAll();
  }

  public WordEntity getWordById(Long wordId) {
    return wordRepo.findById(wordId).orElseThrow(WordNotFoundException::new);
  }

  public List<WordEntity> getAllWordsByListWithAttributes(WordlistEntity wordlist) {
    return wordRepo.findAllByListWithAttributes(wordlist);
  }

  public List<WordEntity> getAllWordsByUser(UserEntity user) {
    return wordRepo.findAllByUser(user);
  }

  public WordEntity createWord(
          UserEntity user,
          WordlistEntity wordlist,
          WordCreateDto dto,
          AttributeWithValuesDto wordAttributes
  ) {
    WordEntity newWord = new WordEntity(dto.value());
    newWord.setUser(user);
    newWord.setWordlist(wordlist);
    return wordRepo.save(
            wordsAttributesService.addAttributes(newWord, wordAttributes)
    );
  }

  public WordEntity updateWord(Long wordId, WordUpdateDto dto) {
    WordEntity candidate = getWordById(wordId);
    candidate.setValue(dto.value());
    return wordRepo.save(candidate);
  }

  public WordEntity removeWord(Long wordId) {
    WordEntity candidate = getWordById(wordId);
    wordRepo.delete(candidate);
    return candidate;
  }
}
