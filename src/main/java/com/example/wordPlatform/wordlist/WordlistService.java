package com.example.wordPlatform.wordlist;

import com.example.wordPlatform.attribute.AttributeEntity;
import com.example.wordPlatform.exception.IllegalAttributesException;
import com.example.wordPlatform.exception.notFound.WordlistNotFoundException;
import com.example.wordPlatform.user.UserEntity;
import com.example.wordPlatform.wordlist.dto.WordlistCreateDto;
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

  public void validateWordlistAttributes(WordlistEntity wordlist, List<AttributeEntity> receivedAttributes) {
    List<AttributeEntity> wordlistAttributes = wordlist.getAttributes();
    int wordlistAttributesCount = wordlistAttributes.size();
    int receivedAttributesCount = receivedAttributes.size();

    if (wordlistAttributesCount == 0) {
      receivedAttributes.forEach(wordlist::addAttribute);
      return;
    }

    if (receivedAttributesCount > wordlistAttributesCount)
      throw new IllegalArgumentException("Illegal to add new attributes to existed word list");


    if (receivedAttributesCount < wordlistAttributesCount) {
      List<String> unprovidedAttributes = wordlistAttributes.stream()
              .filter(attr -> !receivedAttributes.contains(attr))
              .map(AttributeEntity::getName)
              .toList();

      throw new IllegalAttributesException(
              "You didn't provide next attributes",
              unprovidedAttributes
      );
    }


    List<String> unsupportedAttributes = receivedAttributes.stream()
            .filter(attr -> !wordlistAttributes.contains(attr))
            .map(AttributeEntity::getName)
            .toList();

    if (!unsupportedAttributes.isEmpty())
      throw new IllegalAttributesException(
              "Wordlist doesn't support next attributes",
              unsupportedAttributes
      );
  }
}
