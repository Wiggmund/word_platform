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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WordService {
  private static final String WORD_NOT_FOUND_BY_ID = "Word not found by id [%s]";

  private final WordRepo wordRepo;
  private final WordsAttributesService wordsAttributesService;

  public List<Word> getAllWords() {
    log.debug("Getting all existed words");
    return wordRepo.findAll();
  }

  public List<Word> getAllWordsByIdAndWordlist(List<Long> wordIds, Wordlist wordlist) {
    log.debug("Getting all words by ids {} for wordlist {}", wordIds, wordlist);
    return wordRepo.findAllByIdInAndWordlist(wordIds, wordlist);
  }

  public Word getWordById(Long wordId) {
    log.debug("Getting word by id {}", wordId);
    return wordRepo.findById(wordId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(WORD_NOT_FOUND_BY_ID, wordId)));
  }

  public List<Word> getAllWordsByListWithAttributes(Wordlist wordlist) {
    log.debug("Getting all words for wordlist {}", wordlist);
    return wordRepo.findAllByListWithAttributes(wordlist);
  }

  public List<Word> getAllWordsByUser(User user) {
    log.debug("Getting all words for user {}", user);
    return wordRepo.findAllByUser(user);
  }

  public Word createWord(
      User user,
      Wordlist wordlist,
      WordCreateDto dto,
      AttributeWithValuesDto wordAttributes
  ) {
    log.debug("Creating word for user {} and for wordlist {}", user, wordlist);
    Word newWord = Word.builder()
        .definition(dto.value())
        .user(user)
        .wordlist(wordlist)
        .build();

    log.debug("Word was created: {}", newWord);
    return wordRepo.save(
        wordsAttributesService.addAttributes(newWord, wordAttributes)
    );
  }

  public Word updateWord(Long wordId, WordUpdateDto dto) {
    Word candidate = getWordById(wordId);

    log.debug("Updating word {}", candidate);
    candidate.setDefinition(dto.value());
    log.debug("Word was updated {}", candidate);

    return wordRepo.save(candidate);
  }

  public Word removeWord(Long wordId) {
    Word candidate = getWordById(wordId);

    log.debug("Removing word {}", candidate);
    wordRepo.delete(candidate);
    log.debug("Word was removed {}", candidate);

    return candidate;
  }
}
