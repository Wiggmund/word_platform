package com.example.word_platform.repository;

import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import com.example.word_platform.model.word.Word;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WordRepo extends JpaRepository<Word, Long> {
  @Query("SELECT word "
      + "FROM Word word "
      + "LEFT JOIN FETCH word.attributes wordsAttrs "
      + "LEFT JOIN FETCH wordsAttrs.attribute attr "
      + "WHERE word.wordlist = :wordlist")
  List<Word> findAllByWordlistWithAttributes(@Param("wordlist") Wordlist wordlist);

  List<Word> findAllByUser(AppUser user);

  List<Word> findAllByIdInAndWordlist(List<Long> wordIds, Wordlist wordlist);

  @Query("SELECT word "
      + "FROM Word word "
      + "LEFT JOIN FETCH word.attributes wordsAttrs "
      + "LEFT JOIN FETCH wordsAttrs.attribute attr "
      + "WHERE word.wordlist = :wordlist "
      + "AND word.definition = :definition "
      + "AND ("
      + "SELECT COUNT(wa) "
      + "FROM WordsAttributes wa "
      + "WHERE wa.word = word "
      + "AND wa.text NOT IN :attributeValues"
      + ") = 0")
  Optional<Word> findByWordlistAndDefinitionAndAttributeValues(
      @Param("definition") String definition,
      @Param("wordlist") Wordlist wordlist,
      @Param("attributeValues") List<String> attributeValues
  );
}
