package com.example.wordPlatform.user.service;

import com.example.wordPlatform.attribute.AttributeEntity;
import com.example.wordPlatform.attribute.AttributeService;
import com.example.wordPlatform.user.UserEntity;
import com.example.wordPlatform.word.dto.WordCreateDto;
import com.example.wordPlatform.word.WordEntity;
import com.example.wordPlatform.word.service.WordService;
import com.example.wordPlatform.word.dto.WordsAttributesEntryDto;
import com.example.wordPlatform.wordlist.WordlistEntity;
import com.example.wordPlatform.wordlist.WordlistService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserListWordService {
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

    Map<String, String> attributesValues = new HashMap<>();
    dto.attributes()
            .forEach(item -> attributesValues.put(item.name(), item.value()));

    Map<String, String> receivedAttributesMap = dto.attributes().stream()
            .collect(Collectors.toMap(
                    WordsAttributesEntryDto::name,
                    WordsAttributesEntryDto::type
            ));
    List<AttributeEntity> attributes = attributeService.getAttributesIfExistOrCreate(receivedAttributesMap);

    Map<AttributeEntity, String> attributesWithValues = new HashMap<>();
    attributes.forEach(attributeEntity ->
            attributesWithValues.put(
                    attributeEntity,
                    attributesValues.get(attributeEntity.getName())));

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
}
