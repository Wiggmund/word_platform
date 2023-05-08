package com.example.word_platform.repository;

import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Question;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long> {
  Optional<Question> findByUserAndWordlistAndAnswer(AppUser appUser, Wordlist wordlist,
                                                    Attribute attribute);

  List<Question> findAllByWordlist(Wordlist wordlist);

  List<Question> findAllByIdInAndWordlist(List<Long> questionIds, Wordlist wordlist);
}
