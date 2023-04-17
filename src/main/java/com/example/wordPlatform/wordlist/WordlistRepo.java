package com.example.wordPlatform.wordlist;

import com.example.wordPlatform.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordlistRepo extends JpaRepository<WordlistEntity, Long> {
  List<WordlistEntity> findAllByUser(UserEntity user);
}
