package com.example.word_platform.repository;

import com.example.word_platform.model.User;
import com.example.word_platform.model.word.Word;
import com.example.word_platform.model.Wordlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WordRepo extends JpaRepository<Word, Long> {
  @Query("SELECT word " +
          "FROM Word word " +
          "LEFT JOIN FETCH word.attributes wordsAttrs " +
          "LEFT JOIN FETCH wordsAttrs.attribute attr " +
          "WHERE word.wordlist = :wordlist")
  List<Word> findAllByListWithAttributes(@Param("wordlist") Wordlist wordlist);

  List<Word> findAllByUser(User user);

  List<Word> findAllByIdInAndWordlist(List<Long> wordIds, Wordlist wordlist);

  @Query("SELECT word " +
          "FROM Word word " +
          "LEFT JOIN FETCH word.attributes wordsAttrs " +
          "LEFT JOIN FETCH wordsAttrs.attribute attr " +
          "WHERE word.wordlist = :wordlist " +
          "AND word.value = :wordValue " +
          "AND (" +
            "SELECT COUNT(wa) " +
            "FROM WordsAttributes wa " +
            "WHERE wa.word = word " +
            "AND wa.value NOT IN :attributeValues" +
          ") = 0")
  Optional<Word> findByWordlistAndValueAndAttributeValues(
          @Param("wordValue") String wordValue,
          @Param("wordlist") Wordlist wordlist,
          @Param("attributeValues") List<String> attributeValues
  );
}
