package com.example.word_platform.model;

import com.example.word_platform.model.word.Word;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "wordlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wordlist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;

  @OneToMany(mappedBy = "wordlist")
  @ToString.Exclude
  @Builder.Default
  private List<Word> words = new ArrayList<>();

  @OneToMany(mappedBy = "wordlist")
  @ToString.Exclude
  @Builder.Default
  private List<Question> questions = new ArrayList<>();

  @OneToMany(mappedBy = "wordlist")
  @ToString.Exclude
  @Builder.Default
  private List<Stats> statsRecords = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private AppUser user;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "wordlists_attributes",
      joinColumns = @JoinColumn(name = "wordlist_id"),
      inverseJoinColumns = @JoinColumn(name = "attribute_id")
  )
  @ToString.Exclude
  @Builder.Default
  private List<Attribute> attributes = new ArrayList<>();

  public void addWord(Word word) {
    words.add(word);
    word.setWordlist(this);
  }

  public void removeWord(Word word) {
    words.remove(word);
    word.setWordlist(null);
  }

  public void addAttribute(Attribute attribute) {
    attributes.add(attribute);
    attribute.getWordlists().add(this);
  }

  public void removeAttribute(Attribute attribute) {
    attributes.remove(attribute);
    attribute.getWordlists().remove(this);
  }

  public void addQuestion(Question question) {
    questions.add(question);
    question.setWordlist(this);
  }

  public void removeQuestion(Question question) {
    questions.remove(question);
    question.setWordlist(null);
  }

  public void addStatsRecords(List<Stats> records) {
    statsRecords.addAll(records);
    records.forEach(item -> item.setWordlist(this));
  }

  public void removeStatsRecords(List<Stats> records) {
    statsRecords.removeAll(records);
    records.forEach(item -> item.setWordlist(null));
  }
}
