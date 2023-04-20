package com.example.word_platform.attribute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttributeRepo extends JpaRepository<AttributeEntity, Long> {
  Optional<AttributeEntity> findByName(String name);

  @Query("SELECT attr " +
          "FROM AttributeEntity attr " +
          "WHERE attr.name IN :names")
  List<AttributeEntity> findAllByNameIn(@Param("names") List<String> names);
}
