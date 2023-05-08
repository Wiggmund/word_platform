package com.example.word_platform.service.impl;

import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.dto.wordlist.WordlistCreateDto;
import com.example.word_platform.dto.wordlist.WordlistUpdateDto;
import com.example.word_platform.exception.DatabaseRepositoryException;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.exception.WordlistAttributesException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.repository.WordlistRepo;
import com.example.word_platform.service.WordlistService;
import com.example.word_platform.shared.DuplicationCheckService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WordlistServiceImpl implements WordlistService {
  private static final String WORDLIST_DELETING_EXCEPTION =
      "Can't delete wordlist cause of relationship";
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
    log.debug("Getting all existed wordlists");
    return wordlistRepo.findAll();
  }

  public Wordlist getWordlistById(Long wordlistId) {
    log.debug("Getting wordlist by id {}", wordlistId);
    return wordlistRepo.findById(wordlistId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(WORDLIST_NOT_FOUND_BY_ID, wordlistId)));
  }

  public List<Wordlist> getAllWordlistsByUser(AppUser appUser) {
    log.debug("Getting all wordlists for user {}", appUser);
    return wordlistRepo.findAllByUser(appUser);
  }

  public Wordlist getWordlistByIdAndUser(Long wordlistId, AppUser appUser) {
    log.debug("Getting wordlist by id {} and user {}", wordlistId, appUser);
    return wordlistRepo.findByIdAndUser(wordlistId, appUser).orElseThrow(() ->
        new ResourceNotFoundException(String.format(
            WORDLIST_NOT_FOUND_BY_ID_AND_USER,
            wordlistId,
            appUser.getUsername()
        )));
  }

  public Wordlist createWordlist(
      AppUser appUser,
      WordlistCreateDto dto
  ) {
    log.debug("Creating wordlist for user {}", appUser);
    duplicationCheckService.checkWordlistForTitle(dto.title());

    Wordlist newWordlist = Wordlist.builder()
        .title(dto.title())
        .description(dto.description())
        .appUser(appUser)
        .build();

    log.debug("Wordlist was created {}", newWordlist);
    return wordlistRepo.save(newWordlist);
  }

  public Wordlist updateWordlist(Long wordlistId, WordlistUpdateDto dto) {
    Wordlist candidate = getWordlistById(wordlistId);
    log.debug("Updating wordlist {}", candidate);
    WordlistUpdateDto candidateDto =
        new WordlistUpdateDto(candidate.getTitle(), candidate.getDescription());

    if (candidateDto.equals(dto)) {
      return candidate;
    }

    duplicationCheckService.checkWordlistForTitle(dto.title());

    candidate.setTitle(dto.title());
    candidate.setDescription(dto.description());

    log.debug("Wordlist was updated {}", candidate);
    return wordlistRepo.save(candidate);
  }

  public Wordlist removeWordlistById(Long wordlistId) {
    Wordlist candidate = getWordlistById(wordlistId);

    log.debug("Removing wordlist {}", candidate);
    try {
      wordlistRepo.deleteById(wordlistId);
      wordlistRepo.flush();
    } catch (DataIntegrityViolationException ex) {
      throw new DatabaseRepositoryException(WORDLIST_DELETING_EXCEPTION);
    }
    log.debug("Wordlist was removed {}", candidate);

    return candidate;
  }

  public Wordlist save(Wordlist wordlist) {
    log.debug("Saving wordlist {}", wordlist);
    return wordlistRepo.save(wordlist);
  }

  public void initializeWordlistAttributes(Wordlist wordlist, List<Attribute> attributes) {
    log.debug("Initializing attributes {} for wordlist {}", attributes, wordlist);
    attributes.forEach(wordlist::addAttribute);
    wordlistRepo.save(wordlist);
  }

  public void checkIfWordlistSupportAttributes(
      Wordlist wordlist,
      List<WordsAttributesCreateDto> attributesDtos
  ) {
    log.debug("Checking if wordlist {} supports provided attributes {}", wordlist, attributesDtos);
    List<Attribute> wordlistAttributes = wordlist.getAttributes();

    if (wordlistAttributes.isEmpty()) {
      log.debug("Wordlist {} doesn't have any attributes yet", wordlist);
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
    log.debug("Wordlist {} supports all provided attributes {}", wordlist, attributesDtos);
  }

  public void checkIfAllAttributesProvided(
      Wordlist wordlist,
      List<WordsAttributesCreateDto> wordsAttributesCreateDtos
  ) {
    log.debug("Checking if all required attributes for wordlist {} were provided", wordlist);
    checkIfWordlistSupportAttributes(wordlist, wordsAttributesCreateDtos);

    List<Attribute> wordlistAttributes = wordlist.getAttributes();
    long wordlistAttributesCount = wordlistAttributes.size();
    long attributeDtosCount = wordsAttributesCreateDtos.size();

    if (wordlistAttributes.isEmpty()) {
      log.debug("Wordlist {} doesn't have any attributes yet", wordlist);
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
    log.debug("All required attributes for wordlist {} were provided", wordlist);
  }
}
