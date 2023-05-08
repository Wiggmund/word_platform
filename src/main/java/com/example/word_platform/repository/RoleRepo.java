package com.example.word_platform.repository;

import com.example.word_platform.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}
