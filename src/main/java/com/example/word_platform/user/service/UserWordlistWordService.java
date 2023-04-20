package com.example.word_platform.user.service;

import com.example.word_platform.attribute.AttributeEntity;
import com.example.word_platform.attribute.AttributeService;
import com.example.word_platform.attribute.dto.AttributeWithValuesDto;
import com.example.word_platform.user.UserEntity;
import com.example.word_platform.word.dto.WordAttributesUpdateDto;
import com.example.word_platform.word.dto.WordCreateDto;
import com.example.word_platform.word.WordEntity;
import com.example.word_platform.word.dto.WordUpdateDto;
import com.example.word_platform.word.service.WordService;
import com.example.word_platform.word.dto.WordsAttributesCreateDto;
import com.example.word_platform.word.service.WordsAttributesService;
import com.example.word_platform.wordlist.WordlistEntity;
import com.example.word_platform.wordlist.WordlistService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserWordlistWordService {
  private final UserService userService;
  private final WordlistService wordlistService;
  private final AttributeService attributeService;
  private final WordService wordService;
  private final WordsAttributesService wordsAttributesService;

  public List<WordEntity> getAllWordlistWords(Long userId, Long wordlistId) {
    userService.getUserById(userId);
    WordlistEntity wordlist = wordlistService.getWordlistById(wordlistId);

    return wordService.getAllWordsByListWithAttributes(wordlist);
  }

  public WordEntity createAndAddWord(
          Long userId,
          Long wordlistId,
          WordCreateDto dto
  ) {
    UserEntity user = userService.getUserById(userId);
    WordlistEntity wordlist = wordlistService.getWordlistById(wordlistId);
    List<WordsAttributesCreateDto> attributeDtos = dto.attributes();

    wordlistService.checkIfAllAttributesProvided(wordlist, attributeDtos);
    List<AttributeEntity> fetchedAttributes = attributeService.getAttributesFromOrCreate(attributeDtos);

    if (wordlist.getAttributes().isEmpty())
      wordlistService.initializeWordlistAttributes(wordlist, fetchedAttributes);

    WordEntity createdWord = wordService.createWord(
            user,
            wordlist,
            dto,
            new AttributeWithValuesDto(fetchedAttributes, attributeDtos)
    );
    user.addWord(createdWord);
    wordlist.addWord(createdWord);
    userService.save(user);
    wordlistService.save(wordlist);

    return createdWord;
  }

  public WordEntity updateWord(Long userId, Long wordlistId, Long wordId, WordUpdateDto dto) {
    userService.getUserById(userId);
    wordlistService.getWordlistById(wordlistId);
    return wordService.updateWord(wordId, dto);
  }

  public WordEntity updateWordAttributes(Long userId, Long wordlistId, Long wordId, WordAttributesUpdateDto dto) {
    userService.getUserById(userId);
    WordlistEntity wordlist = wordlistService.getWordlistById(wordlistId);
    wordlistService.checkIfWordlistSupportAttributes(wordlist, dto.attributes());
    WordEntity word = wordService.getWordById(wordId);
    return wordsAttributesService.updateAttributes(word, dto.attributes());
  }

  public WordEntity removeWord(Long userId, Long wordlistId, Long wordId) {
    userService.getUserById(userId);
    wordlistService.getWordlistById(wordlistId);
    return wordService.removeWord(wordId);
  }
}
