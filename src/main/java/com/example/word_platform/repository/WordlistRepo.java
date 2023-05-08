package com.example.word_platform.repository;

import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordlistRepo extends JpaRepository<Wordlist, Long> {
  List<Wordlist> findAllByUser(AppUser appUser);

  Optional<Wordlist> findByIdAndUser(Long wordlistId, AppUser appUser);

  Optional<Wordlist> findByTitleIgnoreCase(String title);
}
