package com.example.word_platform.repository;

import com.example.word_platform.model.Stats;
import com.example.word_platform.model.AppUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsRepo extends JpaRepository<Stats, Long> {
  List<Stats> findAllByUser(AppUser user);
}
