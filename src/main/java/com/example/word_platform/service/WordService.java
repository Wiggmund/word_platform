package com.example.word_platform.service;

import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.dto.word.WordCreateDto;
import com.example.word_platform.dto.word.WordUpdateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.repository.WordRepo;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WordService {
  private static final String WORD_NOT_FOUND_BY_ID = "Word not found by id [%s]";

  private final WordRepo wordRepo;
  private final WordsAttributesService wordsAttributesService;

  public List<Word> getAllWords() {
    return wordRepo.findAll();
  }

  public List<Word> getAllWordsByIdAndWordlist(List<Long> wordIds, Wordlist wordlist) {
    return wordRepo.findAllByIdInAndWordlist(wordIds, wordlist);
  }

  public Word getWordById(Long wordId) {
    return wordRepo.findById(wordId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(WORD_NOT_FOUND_BY_ID, wordId)));
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
