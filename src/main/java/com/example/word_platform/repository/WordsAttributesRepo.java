package com.example.word_platform.repository;

import com.example.word_platform.model.word.Word;
import com.example.word_platform.model.word.WordsAttributes;
import com.example.word_platform.model.word.WordsAttributesIdClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordsAttributesRepo extends JpaRepository<WordsAttributes, WordsAttributesIdClass> {
  List<WordsAttributes> findAllByWord(Word word);
}
