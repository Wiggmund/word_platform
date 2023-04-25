package com.example.word_platform.repository;

import com.example.word_platform.model.Attribute;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttributeRepo extends JpaRepository<Attribute, Long> {
  Optional<Attribute> findByName(String name);

  @Query("SELECT attr "
      + "FROM Attribute attr "
      + "WHERE attr.name IN :names")
  List<Attribute> findAllByNameIn(@Param("names") List<String> names);
}
