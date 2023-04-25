package com.example.word_platform.model;

import com.example.word_platform.model.word.Word;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String email;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Wordlist> wordlists = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Word> words = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Question> questions = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Stats> statsRecords = new ArrayList<>();

  public User(String username, String email) {
    this.username = username;
    this.email = email;
  }

  public void addWord(Word word) {
    words.add(word);
    word.setUser(this);
  }

  public void removeWord(Word word) {
    words.remove(word);
    word.setUser(null);
  }

  public void addWordlist(Wordlist wordlist) {
    wordlists.add(wordlist);
    wordlist.setUser(this);
  }

  public void removeWordlist(Wordlist wordlist) {
    wordlists.remove(wordlist);
    wordlist.setUser(null);
  }

  public void addQuestion(Question question) {
    questions.add(question);
    question.setUser(this);
  }

  public void removeQuestion(Question question) {
    questions.remove(question);
    question.setUser(null);
  }

  public void addStatsRecords(List<Stats> records) {
    statsRecords.addAll(records);
    records.forEach(item -> item.setUser(this));
  }

  public void removeStatsRecords(List<Stats> records) {
    statsRecords.removeAll(records);
    records.forEach(item -> item.setUser(null));
  }
}
