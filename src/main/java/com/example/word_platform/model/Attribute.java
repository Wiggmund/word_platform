package com.example.word_platform.model;

import com.example.word_platform.model.word.WordsAttributes;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attributes")
@Data
@NoArgsConstructor
public class Attribute {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String type;

  @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WordsAttributes> words = new ArrayList<>();

  @OneToMany(mappedBy = "answer")
  private List<Question> questions = new ArrayList<>();

  @ManyToMany(mappedBy = "attributes")
  private List<Wordlist> wordlists = new ArrayList<>();

  public Attribute(String name, String type) {
    this.name = name;
    this.type = type;
  }
}
