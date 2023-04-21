package com.example.word_platform.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByUsernameOrEmail(String username, String email);

  Optional<UserEntity> findByUsername(String username);
  Optional<UserEntity> findByEmail(String email);
}
