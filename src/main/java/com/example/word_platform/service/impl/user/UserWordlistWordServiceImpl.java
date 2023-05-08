package com.example.word_platform.service.impl.user;

import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.dto.word.WordCreateDto;
import com.example.word_platform.dto.word.WordUpdateDto;
import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.exception.WordlistAttributesException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.model.word.WordsAttributes;
import com.example.word_platform.service.AttributeService;
import com.example.word_platform.service.WordService;
import com.example.word_platform.service.WordlistService;
import com.example.word_platform.service.WordsAttributesService;
import com.example.word_platform.service.user.UserService;
import com.example.word_platform.service.user.UserWordlistWordService;
import com.example.word_platform.shared.DuplicationCheckService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserWordlistWordServiceImpl implements UserWordlistWordService {
  private static final String REDUNDANT_ATTRIBUTES =
      "You provide redundant attributes. Wordlist require [%s] but you provide [%s]";

  private final UserService userService;
  private final WordlistService wordlistService;
  private final AttributeService attributeService;
  private final WordService wordService;
  private final WordsAttributesService wordsAttributesService;
  private final DuplicationCheckService duplicationCheckService;

  public List<Word> getAllWordlistWords(Long userId, Long wordlistId) {
    userService.getUserById(userId);
    Wordlist wordlist = wordlistService.getWordlistById(wordlistId);

    return wordService.getAllWordsByListWithAttributes(wordlist);
  }

  public Word createAndAddWord(
      Long userId,
      Long wordlistId,
      WordCreateDto dto
  ) {
    AppUser user = userService.getUserById(userId);
    Wordlist wordlist = wordlistService.getWordlistById(wordlistId);
    List<WordsAttributesCreateDto> attributeDtos = dto.attributes();

    wordlistService.checkIfAllAttributesProvided(wordlist, attributeDtos);
    List<Attribute> fetchedAttributes = attributeService.getAttributesFromOrCreate(attributeDtos);
    AttributeWithValuesDto attributesWithValues =
        new AttributeWithValuesDto(fetchedAttributes, attributeDtos);

    if (wordlist.getAttributes().isEmpty()) {
      wordlistService.initializeWordlistAttributes(wordlist, fetchedAttributes);
    }

    if (!wordlist.getAttributes().isEmpty()) {
      duplicationCheckService.checkWordForAttributeValues(dto.value(), attributesWithValues,
          wordlist);
    }

    Word createdWord = wordService.createWord(
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

  public Word updateWord(Long userId, Long wordlistId, Long wordId, WordUpdateDto dto) {
    userService.getUserById(userId);
    wordlistService.getWordlistById(wordlistId);
    return wordService.updateWord(wordId, dto);
  }

  public Word updateWordAttributes(Long userId, Long wordlistId, Long wordId,
                                   List<WordsAttributesCreateDto> dto) {
    userService.getUserById(userId);
    Wordlist wordlist = wordlistService.getWordlistById(wordlistId);
    wordlistService.checkIfWordlistSupportAttributes(wordlist, dto);
    Word word = wordService.getWordById(wordId);

    List<WordsAttributes> wordsAttributesEntries = word.getAttributes();
    int attributeDtosSize = dto.size();
    int wordsAttributesEntriesSize = wordsAttributesEntries.size();

    if (attributeDtosSize > wordsAttributesEntriesSize) {
      throw new WordlistAttributesException(
          String.format(REDUNDANT_ATTRIBUTES, wordsAttributesEntriesSize, attributeDtosSize));
    }

    Map<String, String> dtoAttributeValues = dto.stream().collect(Collectors.toMap(
        WordsAttributesCreateDto::name,
        WordsAttributesCreateDto::value
    ));

    AttributeWithValuesDto attributesWithValues =
        new AttributeWithValuesDto(wordsAttributesEntries.stream()
            .collect(Collectors.toMap(
                WordsAttributes::getAttribute,
                wordAttribute -> dtoAttributeValues.getOrDefault(
                    wordAttribute.getAttribute().getName(),
                    wordAttribute.getText()))));

    duplicationCheckService.checkWordForAttributeValues(word.getDefinition(), attributesWithValues,
        wordlist);

    return wordsAttributesService.updateAttributes(word, attributesWithValues);
  }

  public Word removeWord(Long userId, Long wordlistId, Long wordId) {
    userService.getUserById(userId);
    wordlistService.getWordlistById(wordlistId);
    return wordService.removeWordById(wordId);
  }
}
