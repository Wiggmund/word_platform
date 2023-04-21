package com.example.word_platform.word;

import com.example.word_platform.user.UserEntity;
import com.example.word_platform.wordlist.WordlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WordRepo extends JpaRepository<WordEntity, Long> {
  @Query("SELECT word " +
          "FROM WordEntity word " +
          "LEFT JOIN FETCH word.attributes wordsAttrs " +
          "LEFT JOIN FETCH wordsAttrs.attribute attr " +
          "WHERE word.wordlist = :wordlist")
  List<WordEntity> findAllByListWithAttributes(@Param("wordlist") WordlistEntity wordlist);

  List<WordEntity> findAllByUser(UserEntity user);
}
