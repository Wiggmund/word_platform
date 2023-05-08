package com.example.word_platform.repository;

import com.example.word_platform.model.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<AppUser, Long> {
  Optional<AppUser> findByUsernameIgnoreCaseOrEmail(String username, String email);

  Optional<AppUser> findByUsernameIgnoreCase(String username);

  Optional<AppUser> findByEmail(String email);
}
