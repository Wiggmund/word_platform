package com.example.word_platform.service.user;

import com.example.word_platform.model.Attribute;
import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.model.User;
import com.example.word_platform.dto.word.WordAttributesUpdateDto;
import com.example.word_platform.dto.word.WordCreateDto;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.dto.word.WordUpdateDto;
import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.service.AttributeService;
import com.example.word_platform.service.WordService;
import com.example.word_platform.service.WordlistService;
import com.example.word_platform.service.WordsAttributesService;
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
    User user = userService.getUserById(userId);
    Wordlist wordlist = wordlistService.getWordlistById(wordlistId);
    List<WordsAttributesCreateDto> attributeDtos = dto.attributes();

    wordlistService.checkIfAllAttributesProvided(wordlist, attributeDtos);
    List<Attribute> fetchedAttributes = attributeService.getAttributesFromOrCreate(attributeDtos);

    if (wordlist.getAttributes().isEmpty())
      wordlistService.initializeWordlistAttributes(wordlist, fetchedAttributes);

    Word createdWord = wordService.createWord(
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

  public Word updateWord(Long userId, Long wordlistId, Long wordId, WordUpdateDto dto) {
    userService.getUserById(userId);
    wordlistService.getWordlistById(wordlistId);
    return wordService.updateWord(wordId, dto);
  }

  public Word updateWordAttributes(Long userId, Long wordlistId, Long wordId, WordAttributesUpdateDto dto) {
    userService.getUserById(userId);
    Wordlist wordlist = wordlistService.getWordlistById(wordlistId);
    wordlistService.checkIfWordlistSupportAttributes(wordlist, dto.attributes());
    Word word = wordService.getWordById(wordId);
    return wordsAttributesService.updateAttributes(word, dto.attributes());
  }

  public Word removeWord(Long userId, Long wordlistId, Long wordId) {
    userService.getUserById(userId);
    wordlistService.getWordlistById(wordlistId);
    return wordService.removeWord(wordId);
  }
}
