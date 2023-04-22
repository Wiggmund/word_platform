package com.example.word_platform.service;

import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.exception.not_found.WordNotFoundException;
import com.example.word_platform.model.User;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.repository.WordRepo;
import com.example.word_platform.dto.word.WordCreateDto;
import com.example.word_platform.dto.word.WordUpdateDto;
import com.example.word_platform.model.Wordlist;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WordService {
  private final WordRepo wordRepo;
  private final WordsAttributesService wordsAttributesService;

  public List<Word> getAllWords() {
    return wordRepo.findAll();
  }

  public Word getWordById(Long wordId) {
    return wordRepo.findById(wordId).orElseThrow(WordNotFoundException::new);
  }

  public List<Word> getAllWordsByListWithAttributes(Wordlist wordlist) {
    return wordRepo.findAllByListWithAttributes(wordlist);
  }

  public List<Word> getAllWordsByUser(User user) {
    return wordRepo.findAllByUser(user);
  }

  public Word createWord(
          User user,
          Wordlist wordlist,
          WordCreateDto dto,
          AttributeWithValuesDto wordAttributes
  ) {
    Word newWord = new Word(dto.value());
    newWord.setUser(user);
    newWord.setWordlist(wordlist);
    return wordRepo.save(
            wordsAttributesService.addAttributes(newWord, wordAttributes)
    );
  }

  public Word updateWord(Long wordId, WordUpdateDto dto) {
    Word candidate = getWordById(wordId);
    candidate.setValue(dto.value());
    return wordRepo.save(candidate);
  }

  public Word removeWord(Long wordId) {
    Word candidate = getWordById(wordId);
    wordRepo.delete(candidate);
    return candidate;
  }
}
