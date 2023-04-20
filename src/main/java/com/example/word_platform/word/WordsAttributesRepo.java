package com.example.word_platform.word;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordsAttributesRepo extends JpaRepository<WordsAttributesEntity, WordsAttributesIdClass> {
  List<WordsAttributesEntity> findAllByWord(WordEntity word);
}
