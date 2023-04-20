package com.example.word_platform.wordlist;

import com.example.word_platform.attribute.AttributeEntity;
import com.example.word_platform.exception.IllegalAttributesException;
import com.example.word_platform.exception.not_found.WordlistNotFoundException;
import com.example.word_platform.user.UserEntity;
import com.example.word_platform.word.dto.WordsAttributesCreateDto;
import com.example.word_platform.wordlist.dto.WordlistCreateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WordlistService {
  private final WordlistRepo wordlistRepo;

  public WordlistEntity getWordlistById(Long wordlistId) {
    return wordlistRepo.findById(wordlistId)
            .orElseThrow(WordlistNotFoundException::new);
  }

  public List<WordlistEntity> getAllWordlistsByUser(UserEntity user) {
    return wordlistRepo.findAllByUser(user);
  }

  public WordlistEntity createWordlist(
          UserEntity user,
          WordlistCreateDto dto
  ) {
    WordlistEntity newWordlist = new WordlistEntity(dto.title(), dto.description());
    newWordlist.setUser(user);

    return wordlistRepo.save(newWordlist);
  }

  public WordlistEntity save(WordlistEntity wordlist) {
    return wordlistRepo.save(wordlist);
  }

  public void initializeWordlistAttributes(WordlistEntity wordlist, List<AttributeEntity> attributes) {
    attributes.forEach(wordlist::addAttribute);
    wordlistRepo.save(wordlist);
  }

  public void checkIfWordlistSupportAttributes(
          WordlistEntity wordlist,
          List<WordsAttributesCreateDto> attributesDtos
  ) {
    List<AttributeEntity> wordlistAttributes = wordlist.getAttributes();

    if (wordlistAttributes.isEmpty()) return;

    List<String> wordlistAttributeNames = wordlistAttributes.stream().map(AttributeEntity::getName).toList();
    List<String> unSupportedAttributes = attributesDtos.stream()
            .map(WordsAttributesCreateDto::name)
            .filter(name -> !wordlistAttributeNames.contains(name))
            .toList();

    if (!unSupportedAttributes.isEmpty())
      throw new IllegalAttributesException(
              "Current wordlist doesn't support these attributes",
              unSupportedAttributes
      );
  }

  public void checkIfAllAttributesProvided(
          WordlistEntity wordlist,
          List<WordsAttributesCreateDto> wordsAttributesCreateDtos
  ) {
    List<AttributeEntity> wordlistAttributes = wordlist.getAttributes();
    long wordlistAttributesCount = wordlistAttributes.size();
    long attributeDtosCount = wordsAttributesCreateDtos.size();

    if (wordlistAttributes.isEmpty()) return;

    if (attributeDtosCount >= wordlistAttributesCount)
      checkIfWordlistSupportAttributes(wordlist, wordsAttributesCreateDtos);

    List<String> dtoAttributeNames = wordsAttributesCreateDtos.stream()
            .map(WordsAttributesCreateDto::name)
            .toList();
    List<String> unProvidedAttributes = wordlistAttributes.stream()
            .map(AttributeEntity::getName)
            .filter(name -> !dtoAttributeNames.contains(name))
            .toList();

    if (!unProvidedAttributes.isEmpty())
      throw new IllegalAttributesException(
              "You didn't provide these attributes",
              unProvidedAttributes
      );
  }
}