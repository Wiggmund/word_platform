package com.example.word_platform.repository;

import com.example.word_platform.model.User;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.model.Wordlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepo extends JpaRepository<Word, Long> {
  @Query("SELECT word " +
          "FROM Word word " +
          "LEFT JOIN FETCH word.attributes wordsAttrs " +
          "LEFT JOIN FETCH wordsAttrs.attribute attr " +
          "WHERE word.wordlist = :wordlist")
  List<Word> findAllByListWithAttributes(@Param("wordlist") Wordlist wordlist);

  List<Word> findAllByUser(User user);
}
