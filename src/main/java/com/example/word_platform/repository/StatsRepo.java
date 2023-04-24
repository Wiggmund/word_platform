package com.example.word_platform.repository;

import com.example.word_platform.model.Stats;
import com.example.word_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatsRepo extends JpaRepository<Stats, Long> {
  List<Stats> findAllByUser(User user);
}
