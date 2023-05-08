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
  private String password;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @Builder.Default
  @ToString.Exclude
  private List<Role> roles = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @ToString.Exclude
  @Builder.Default
  private List<Wordlist> wordlists = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  @ToString.Exclude
  @Builder.Default
  private List<Word> words = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  @ToString.Exclude
  @Builder.Default
  private List<Question> questions = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  @ToString.Exclude
  @Builder.Default
  private List<Stats> statsRecords = new ArrayList<>();

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

  public void addRole(Role role) {
    this.roles.add(role);
    role.getUsers().add(this);
  }

  public void removeRole(Role role) {
    this.roles.remove(role);
    role.getUsers().remove(this);
  }
}
