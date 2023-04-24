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
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wordlists")
@Data
@NoArgsConstructor
public class Wordlist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;

  @OneToMany(mappedBy = "wordlist")
  private List<Word> words = new ArrayList<>();

  @OneToMany(mappedBy = "wordlist")
  private List<Question> questions = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
          name = "wordlists_attributes",
          joinColumns = @JoinColumn(name = "wordlist_id"),
          inverseJoinColumns = @JoinColumn(name = "attribute_id")
  )
  private List<Attribute> attributes = new ArrayList<>();

  public Wordlist(String title, String description) {
    this.title = title;
    this.description = description;
  }

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
}
