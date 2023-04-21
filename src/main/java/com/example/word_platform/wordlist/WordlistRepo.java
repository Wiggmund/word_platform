package com.example.word_platform.wordlist;

import com.example.word_platform.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WordlistRepo extends JpaRepository<WordlistEntity, Long> {
  List<WordlistEntity> findAllByUser(UserEntity user);

  Optional<WordlistEntity> findByIdAndUser(Long wordlistId, UserEntity user);

  Optional<WordlistEntity> findByTitle(String title);
}
