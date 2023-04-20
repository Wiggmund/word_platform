package com.example.wordPlatform.user.service;

import com.example.wordPlatform.attribute.AttributeEntity;
import com.example.wordPlatform.attribute.AttributeService;
import com.example.wordPlatform.user.UserEntity;
import com.example.wordPlatform.word.dto.WordCreateDto;
import com.example.wordPlatform.word.WordEntity;
import com.example.wordPlatform.word.service.WordService;
import com.example.wordPlatform.word.dto.WordsAttributesCreateDto;
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

  public WordEntity createAndAddWord(
          Long userId,
          Long wordlistId,
          WordCreateDto dto
  ) {
    UserEntity user = userService.getUserById(userId);
    WordlistEntity wordlist = wordlistService.getWordlistById(wordlistId);
    List<WordsAttributesCreateDto> receivedAttributes = dto.attributes();

    List<AttributeEntity> fetchedAttributes = attributeService.getAttributesOrCreate(receivedAttributes);
    wordlistService.validateWordlistAttributes(wordlist, fetchedAttributes);

    Map<String, String> receivedAttributeValues = receivedAttributes.stream()
            .collect(Collectors.toMap(
                    WordsAttributesCreateDto::name,
                    WordsAttributesCreateDto::value
            ));

    Map<AttributeEntity, String> fetchedAttributesWithValues = fetchedAttributes.stream()
            .collect(Collectors.toMap(
                    Function.identity(),
                    item -> receivedAttributeValues.get(item.getName())
            ));

    WordEntity createdWord = wordService.createWord(
            user,
            wordlist,
            dto,
            fetchedAttributesWithValues
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
}
