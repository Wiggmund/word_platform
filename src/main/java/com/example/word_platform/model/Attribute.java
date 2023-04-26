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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attribute {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String type;

  @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  @ToString.Exclude
  private List<WordsAttributes> words = new ArrayList<>();

  @OneToMany(mappedBy = "answer")
  @Builder.Default
  @ToString.Exclude
  private List<Question> questions = new ArrayList<>();

  @ManyToMany(mappedBy = "attributes")
  @Builder.Default
  @ToString.Exclude
  private List<Wordlist> wordlists = new ArrayList<>();
}
