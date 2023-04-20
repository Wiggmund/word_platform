package com.example.wordPlatform.word;

import com.example.wordPlatform.wordlist.WordlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepo extends JpaRepository<WordEntity, Long> {
  @Query("SELECT word " +
          "FROM WordEntity word " +
          "LEFT JOIN FETCH word.attributes wordsAttrs " +
          "LEFT JOIN FETCH wordsAttrs.attribute attr " +
          "WHERE word.wordlist = :wordlist")
  List<WordEntity> findAllByListWithAttributes(@Param("wordlist") WordlistEntity wordlist);
}
