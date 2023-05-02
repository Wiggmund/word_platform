package com.example.word_platform.service.user;

import com.example.word_platform.dto.word.WordCreateDto;
import com.example.word_platform.dto.word.WordUpdateDto;
import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.model.word.Word;
import java.util.List;

public interface UserWordlistWordService {
  List<Word> getAllWordlistWords(Long userId, Long wordlistId);

  Word createAndAddWord(Long userId, Long wordlistId, WordCreateDto dto);

  Word updateWord(Long userId, Long wordlistId, Long wordId, WordUpdateDto dto);

  Word updateWordAttributes(Long userId,
                            Long wordlistId,
                            Long wordId,
                            List<WordsAttributesCreateDto> dto);

  Word removeWord(Long userId, Long wordlistId, Long wordId);
}
