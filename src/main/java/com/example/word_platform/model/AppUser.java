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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String email;

  @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @Builder.Default
  private List<Wordlist> wordlists = new ArrayList<>();

  @OneToMany(mappedBy = "appUser")
  @ToString.Exclude
  @Builder.Default
  private List<Word> words = new ArrayList<>();

  @OneToMany(mappedBy = "appUser")
  @ToString.Exclude
  @Builder.Default
  private List<Question> questions = new ArrayList<>();

  @OneToMany(mappedBy = "appUser")
  @ToString.Exclude
  @Builder.Default
  private List<Stats> statsRecords = new ArrayList<>();

  public void addWord(Word word) {
    words.add(word);
    word.setAppUser(this);
  }

  public void removeWord(Word word) {
    words.remove(word);
    word.setAppUser(null);
  }

  public void addWordlist(Wordlist wordlist) {
    wordlists.add(wordlist);
    wordlist.setAppUser(this);
  }

  public void removeWordlist(Wordlist wordlist) {
    wordlists.remove(wordlist);
    wordlist.setAppUser(null);
  }

  public void addQuestion(Question question) {
    questions.add(question);
    question.setAppUser(this);
  }

  public void removeQuestion(Question question) {
    questions.remove(question);
    question.setAppUser(null);
  }

  public void addStatsRecords(List<Stats> records) {
    statsRecords.addAll(records);
    records.forEach(item -> item.setAppUser(this));
  }

  public void removeStatsRecords(List<Stats> records) {
    statsRecords.removeAll(records);
    records.forEach(item -> item.setAppUser(null));
  }
}
