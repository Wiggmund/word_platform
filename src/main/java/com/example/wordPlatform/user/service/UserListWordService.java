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

import java.util.ArrayList;
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

    /*
    * Transforming dto attributes to get AttributeEntities
    * */
    Map<String, String> dtoAttrValues = new HashMap<>();
    dto.attributes()
            .forEach(item -> dtoAttrValues.put(item.name(), item.value()));

    Map<String, String> dtoAttrNamesAndTypes = dto.attributes().stream()
            .collect(Collectors.toMap(
                    WordsAttributesEntryDto::name,
                    WordsAttributesEntryDto::type
            ));
    List<AttributeEntity> fetchedAttrEntities = attributeService.getAttributesIfExistOrCreate(dtoAttrNamesAndTypes);

    /*
    * We need to enforce attribute consistency for WordlistEntity,
    * so that a new word cannot be added with new or incomplete attributes.
    * All attributes of the WordlistEntity must be provided when adding a new word
    * */
    List<AttributeEntity> wordlistAttrs = wordlist.getAttributes();
    int wordlistAttrsCount = wordlistAttrs.size();
    int fetchedAttrsCount = fetchedAttrEntities.size();
    if (wordlistAttrsCount == 0) {
      fetchedAttrEntities.forEach(wordlist::addAttribute);
    } else {
      if (wordlistAttrsCount != fetchedAttrsCount)
          throw new IllegalArgumentException("Illegal to add new columns to the word list");

      List<String> unsupportedAttrs = fetchedAttrEntities.stream()
              .filter(item -> !wordlistAttrs.contains(item))
              .map(AttributeEntity::getName)
              .toList();

      if (!unsupportedAttrs.isEmpty())
        throw new IllegalArgumentException(
                "Wordlist doesn't have next columns: " +
                String.join(", ", unsupportedAttrs)
        );
    }

    Map<AttributeEntity, String> fetchedAttrEntitiesWithValues = new HashMap<>();
    fetchedAttrEntities.forEach(attributeEntity ->
            fetchedAttrEntitiesWithValues.put(
                    attributeEntity,
                    dtoAttrValues.get(attributeEntity.getName())));

    WordEntity createdWord = wordService.createWord(
            user,
            wordlist,
            dto,
            fetchedAttrEntitiesWithValues
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
