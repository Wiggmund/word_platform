package com.example.word_platform.repository;

import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByUser(AppUser user);
}
