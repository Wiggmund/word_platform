package com.example.wordPlatform.user.service;

import com.example.wordPlatform.attribute.AttributeEntity;
import com.example.wordPlatform.attribute.AttributeService;
import com.example.wordPlatform.user.UserEntity;
import com.example.wordPlatform.word.dto.WordAttributesUpdateDto;
import com.example.wordPlatform.word.dto.WordCreateDto;
import com.example.wordPlatform.word.WordEntity;
import com.example.wordPlatform.word.dto.WordUpdateDto;
import com.example.wordPlatform.word.service.WordService;
import com.example.wordPlatform.word.dto.WordsAttributesCreateDto;
import com.example.wordPlatform.word.service.WordsAttributesService;
import com.example.wordPlatform.wordlist.WordlistEntity;
import com.example.wordPlatform.wordlist.WordlistService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserWordlistWordService {
  private final UserService userService;
  private final WordlistService wordlistService;
  private final AttributeService attributeService;
  private final WordService wordService;
  private final WordsAttributesService wordsAttributesService;

  public WordEntity createAndAddWord(
          Long userId,
          Long wordlistId,
          WordCreateDto dto
  ) {
    UserEntity user = userService.getUserById(userId);
    WordlistEntity wordlist = wordlistService.getWordlistById(wordlistId);
    Map<AttributeEntity, String> attributesWithValues = validateAndGetAttributesWithValues(wordlist, dto.attributes());

    WordEntity createdWord = wordService.createWord(
            user,
            wordlist,
            dto,
            attributesWithValues
    );
    user.addWord(createdWord);
    wordlist.addWord(createdWord);
    userService.save(user);
    wordlistService.save(wordlist);

    return createdWord;
  }

  public List<WordEntity> getAllWordlistWords(Long userId, Long wordlistId) {
    userService.getUserById(userId);
    WordlistEntity wordlist = wordlistService.getWordlistById(wordlistId);

    return wordService.getAllWordsByListWithAttributes(wordlist);
  }

  public WordEntity updateWord(Long userId, Long wordlistId, Long wordId, WordUpdateDto dto) {
    userService.getUserById(userId);
    wordlistService.getWordlistById(wordlistId);
    return wordService.updateWord(wordId, dto);
  }

  public WordEntity updateWordAttributes(Long userId, Long wordlistId, Long wordId, WordAttributesUpdateDto dto) {
    userService.getUserById(userId);
    WordlistEntity wordlist = wordlistService.getWordlistById(wordlistId);
    WordEntity word = wordService.getWordById(wordId);
    validateAndGetAttributesWithValues(wordlist, dto.attributes());
    return wordsAttributesService.updateAttributes(word, dto.attributes());
  }

  private Map<AttributeEntity, String> validateAndGetAttributesWithValues(
          WordlistEntity wordlist,
          List<WordsAttributesCreateDto> receivedAttributes
  ) {
    List<AttributeEntity> fetchedAttributes = attributeService.getAttributesOrCreate(receivedAttributes);
    wordlistService.validateWordlistAttributes(wordlist, fetchedAttributes);

    Map<String, String> receivedAttributeValues = receivedAttributes.stream()
            .collect(Collectors.toMap(
                    WordsAttributesCreateDto::name,
                    WordsAttributesCreateDto::value
            ));

    return fetchedAttributes.stream()
            .collect(Collectors.toMap(
                    Function.identity(),
                    item -> receivedAttributeValues.get(item.getName())
            ));
  }

  public WordEntity removeWord(Long userId, Long wordlistId, Long wordId) {
    userService.getUserById(userId);
    wordlistService.getWordlistById(wordlistId);
    return wordService.removeWord(wordId);
  }
}
