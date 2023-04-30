package com.example.word_platform.repository;

import com.example.word_platform.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
  Optional<User> findByUsernameIgnoreCaseOrEmail(String username, String email);

  Optional<User> findByUsernameIgnoreCase(String username);

  Optional<User> findByEmail(String email);
}
