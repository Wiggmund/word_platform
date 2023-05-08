package com.example.word_platform.service;

import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.dto.wordlist.WordlistCreateDto;
import com.example.word_platform.dto.wordlist.WordlistUpdateDto;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import java.util.List;

public interface WordlistService {
  List<Wordlist> getAllWordlists();

  Wordlist getWordlistById(Long wordlistId);

  List<Wordlist> getAllWordlistsByUser(AppUser appUser);

  Wordlist getWordlistByIdAndUser(Long wordlistId, AppUser appUser);

  Wordlist createWordlist(AppUser appUser, WordlistCreateDto dto);

  Wordlist updateWordlist(Long wordlistId, WordlistUpdateDto dto);

  Wordlist removeWordlistById(Long wordlistId);

  Wordlist save(Wordlist wordlist);

  void initializeWordlistAttributes(Wordlist wordlist, List<Attribute> attributes);

  void checkIfWordlistSupportAttributes(Wordlist wordlist,
                                        List<WordsAttributesCreateDto> attributesDtos);

  void checkIfAllAttributesProvided(Wordlist wordlist,
                                    List<WordsAttributesCreateDto> wordsAttributesCreateDtos);
}
