package com.example.wordPlatform.attribute;

import com.example.wordPlatform.word.WordsAttributesEntity;
import com.example.wordPlatform.wordlist.WordlistEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attributes")
@Data
@NoArgsConstructor
public class AttributeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String type;

  @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WordsAttributesEntity> words = new ArrayList<>();

  @ManyToMany(mappedBy = "attributes")
  private List<WordlistEntity> wordlists = new ArrayList<>();

  public AttributeEntity(String name, String type) {
    this.name = name;
    this.type = type;
  }
}
