package com.example.word_platform.service;

import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.dto.wordlist.WordlistCreateDto;
import com.example.word_platform.dto.wordlist.WordlistUpdateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.exception.WordlistAttributesException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.User;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.repository.WordlistRepo;
import com.example.word_platform.shared.DuplicationCheckService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WordlistService {
  private static final String WORDLIST_NOT_FOUND_BY_ID = "Wordlist not found by id [%s]";
  private static final String WORDLIST_NOT_FOUND_BY_ID_AND_USER =
      "Wordlist not found by id [%s] for user [%s]";
  private static final String REDUNDANT_ATTRIBUTES =
      "You provide redundant attributes. Wordlist require [%s] but you provide [%s]";
  private static final String UNPROVIDED_ATTRIBUTES =
      "You  didn't provide [%s] attributes for wordlist [%s]";
  private static final String UNSUPPORTED_ATTRIBUTES =
      "Wordlist [%s] doesn't support next attributes: [%s]";

  private final WordlistRepo wordlistRepo;
  private final DuplicationCheckService duplicationCheckService;

  public List<Wordlist> getAllWordlists() {
    return wordlistRepo.findAll();
  }

  public Wordlist getWordlistById(Long wordlistId) {
    return wordlistRepo.findById(wordlistId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(WORDLIST_NOT_FOUND_BY_ID, wordlistId)));
  }

  public List<Wordlist> getAllWordlistsByUser(User user) {
    return wordlistRepo.findAllByUser(user);
  }

  public Wordlist getWordlistByIdAndUser(Long wordlistId, User user) {
    return wordlistRepo.findByIdAndUser(wordlistId, user).orElseThrow(() ->
        new ResourceNotFoundException(String.format(
            WORDLIST_NOT_FOUND_BY_ID_AND_USER,
            wordlistId,
            user.getUsername()
        )));
  }

  public Wordlist createWordlist(
      User user,
      WordlistCreateDto dto
  ) {
    duplicationCheckService.checkWordlistForTitle(dto.title());
    Wordlist newWordlist = new Wordlist(dto.title(), dto.description());
    newWordlist.setUser(user);

    return wordlistRepo.save(newWordlist);
  }

  public Wordlist updateWordlist(Long wordlistId, WordlistUpdateDto dto) {
    Wordlist candidate = getWordlistById(wordlistId);
    WordlistUpdateDto candidateDto =
        new WordlistUpdateDto(candidate.getTitle(), candidate.getDescription());

    if (candidateDto.equals(dto)) {
      return candidate;
    }

    duplicationCheckService.checkWordlistForTitle(dto.title());

    candidate.setTitle(dto.title());
    candidate.setDescription(dto.description());

    return wordlistRepo.save(candidate);
  }

  public Wordlist removeWordlist(Long wordlistId) {
    Wordlist candidate = getWordlistById(wordlistId);
    wordlistRepo.delete(candidate);
    return candidate;
  }

  public Wordlist save(Wordlist wordlist) {
    return wordlistRepo.save(wordlist);
  }

  public void initializeWordlistAttributes(Wordlist wordlist, List<Attribute> attributes) {
    attributes.forEach(wordlist::addAttribute);
    wordlistRepo.save(wordlist);
  }

  public void checkIfWordlistSupportAttributes(
      Wordlist wordlist,
      List<WordsAttributesCreateDto> attributesDtos
  ) {
    List<Attribute> wordlistAttributes = wordlist.getAttributes();

    if (wordlistAttributes.isEmpty()) {
      return;
    }

    List<String> wordlistAttributeNames =
        wordlistAttributes.stream().map(Attribute::getName).toList();
    List<String> unSupportedAttributes = attributesDtos.stream()
        .map(WordsAttributesCreateDto::name)
        .filter(name -> !wordlistAttributeNames.contains(name))
        .toList();

    if (!unSupportedAttributes.isEmpty()) {
      throw new WordlistAttributesException(String.format(
          UNSUPPORTED_ATTRIBUTES,
          wordlist.getTitle(),
          String.join(", ", unSupportedAttributes)
      ));
    }
  }

  public void checkIfAllAttributesProvided(
      Wordlist wordlist,
      List<WordsAttributesCreateDto> wordsAttributesCreateDtos
  ) {
    checkIfWordlistSupportAttributes(wordlist, wordsAttributesCreateDtos);

    List<Attribute> wordlistAttributes = wordlist.getAttributes();
    long wordlistAttributesCount = wordlistAttributes.size();
    long attributeDtosCount = wordsAttributesCreateDtos.size();

    if (wordlistAttributes.isEmpty()) {
      return;
    }

    if (attributeDtosCount > wordlistAttributesCount) {
      throw new WordlistAttributesException(String.format(
          REDUNDANT_ATTRIBUTES,
          wordlistAttributesCount,
          attributeDtosCount
      ));
    }

    List<String> dtoAttributeNames = wordsAttributesCreateDtos.stream()
        .map(WordsAttributesCreateDto::name)
        .toList();
    List<String> unProvidedAttributes = wordlistAttributes.stream()
        .map(Attribute::getName)
        .filter(name -> !dtoAttributeNames.contains(name))
        .toList();

    if (!unProvidedAttributes.isEmpty()) {
      throw new WordlistAttributesException(String.format(
          UNPROVIDED_ATTRIBUTES,
          String.join(", ", unProvidedAttributes),
          wordlist.getTitle()
      ));
    }
  }
}
