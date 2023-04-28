package com.example.word_platform.repository;

import com.example.word_platform.model.Attribute;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AttributeRepo extends JpaRepository<Attribute, Long> {
  Optional<Attribute> findByNameIgnoreCase(String name);

  List<Attribute> findAllByNameIgnoreCaseIn(@Param("names") List<String> names);
}
