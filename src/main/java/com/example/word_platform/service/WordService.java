package com.example.word_platform.service;

import com.example.word_platform.dto.attribute.AttributeWithValuesDto;
import com.example.word_platform.dto.word.WordCreateDto;
import com.example.word_platform.dto.word.WordUpdateDto;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import java.util.List;

public interface WordService {
  List<Word> getAllWords();

  List<Word> getAllWordsByIdAndWordlist(List<Long> wordIds, Wordlist wordlist);

  Word getWordById(Long wordId);

  List<Word> getAllWordsByListWithAttributes(Wordlist wordlist);

  List<Word> getAllWordsByUser(AppUser user);

  Word createWord(AppUser user,
                  Wordlist wordlist,
                  WordCreateDto dto,
                  AttributeWithValuesDto wordAttributes);

  Word updateWord(Long wordId, WordUpdateDto dto);

  Word removeWordById(Long wordId);
}



