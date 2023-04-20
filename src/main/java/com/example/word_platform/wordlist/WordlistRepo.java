package com.example.word_platform.wordlist;

import com.example.word_platform.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordlistRepo extends JpaRepository<WordlistEntity, Long> {
  List<WordlistEntity> findAllByUser(UserEntity user);
}
