package com.example.word_platform.model.word;

import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.Stats;
import com.example.word_platform.model.AppUser;
import com.example.word_platform.model.Wordlist;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "words")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String definition;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private AppUser appUser;

  @ManyToOne
  @JoinColumn(name = "wordlist_id")
  @ToString.Exclude
  private Wordlist wordlist;

  @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @Builder.Default
  private List<WordsAttributes> attributes = new ArrayList<>();

  @OneToMany(mappedBy = "word")
  @ToString.Exclude
  @Builder.Default
  private List<Stats> statsRecords = new ArrayList<>();

  public WordsAttributes addAttribute(Attribute attribute, String value) {
    WordsAttributes wordAttribute = new WordsAttributes(this, attribute, value);
    attributes.add(wordAttribute);
    attribute.getWords().add(wordAttribute);
    return wordAttribute;
  }

  public void removeAttribute(Attribute attribute, String value) {
    WordsAttributes wordAttribute = new WordsAttributes(this, attribute, value);
    attribute.getWords().remove(wordAttribute);
    attributes.remove(wordAttribute);
    wordAttribute.setWord(null);
    wordAttribute.setAttribute(null);
  }
}
