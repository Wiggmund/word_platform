package com.example.word_platform.service;

import com.example.word_platform.model.Attribute;
import com.example.word_platform.exception.IllegalAttributesException;
import com.example.word_platform.exception.not_found.WordlistNotFoundException;
import com.example.word_platform.shared.DuplicationCheckService;
import com.example.word_platform.model.User;
import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.repository.WordlistRepo;
import com.example.word_platform.dto.wordlist.WordlistCreateDto;
import com.example.word_platform.dto.wordlist.WordlistUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WordlistService {
  private final WordlistRepo wordlistRepo;
  private final DuplicationCheckService duplicationCheckService;

  public List<Wordlist> getAllWordlists() {
    return wordlistRepo.findAll();
  }

  public Wordlist getWordlistById(Long wordlistId) {
    return wordlistRepo.findById(wordlistId)
            .orElseThrow(WordlistNotFoundException::new);
  }

  public List<Wordlist> getAllWordlistsByUser(User user) {
    return wordlistRepo.findAllByUser(user);
  }

  public Wordlist getWordlistByIdAndUser(Long wordlistId, User user) {
    return wordlistRepo.findByIdAndUser(wordlistId, user)
            .orElseThrow(WordlistNotFoundException::new);
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
    WordlistUpdateDto candidateDto = new WordlistUpdateDto(candidate.getTitle(), candidate.getDescription());

    if (candidateDto.equals(dto)) return candidate;

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

    if (wordlistAttributes.isEmpty()) return;

    List<String> wordlistAttributeNames = wordlistAttributes.stream().map(Attribute::getName).toList();
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
          Wordlist wordlist,
          List<WordsAttributesCreateDto> wordsAttributesCreateDtos
  ) {
    List<Attribute> wordlistAttributes = wordlist.getAttributes();
    long wordlistAttributesCount = wordlistAttributes.size();
    long attributeDtosCount = wordsAttributesCreateDtos.size();

    if (wordlistAttributes.isEmpty()) return;

    if (attributeDtosCount >= wordlistAttributesCount)
      checkIfWordlistSupportAttributes(wordlist, wordsAttributesCreateDtos);

    List<String> dtoAttributeNames = wordsAttributesCreateDtos.stream()
            .map(WordsAttributesCreateDto::name)
            .toList();
    List<String> unProvidedAttributes = wordlistAttributes.stream()
            .map(Attribute::getName)
            .filter(name -> !dtoAttributeNames.contains(name))
            .toList();

    if (!unProvidedAttributes.isEmpty())
      throw new IllegalAttributesException(
              "You didn't provide these attributes",
              unProvidedAttributes
      );
  }
}
