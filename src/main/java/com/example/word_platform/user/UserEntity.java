package com.example.word_platform.user;

import com.example.word_platform.word.WordEntity;
import com.example.word_platform.wordlist.WordlistEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String email;

  @OneToMany(mappedBy = "user")
  private List<WordlistEntity> wordlists = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<WordEntity> words = new ArrayList<>();

  public UserEntity(String username, String email) {
    this.username = username;
    this.email = email;
  }

  public void addWord(WordEntity word) {
    words.add(word);
    word.setUser(this);
  }

  public void removeWord(WordEntity word) {
    words.remove(word);
    word.setUser(null);
  }

  public void addWordlist(WordlistEntity wordlist) {
    wordlists.add(wordlist);
    wordlist.setUser(this);
  }

  public void removeWordlist(WordlistEntity wordlist) {
    wordlists.remove(wordlist);
    wordlist.setUser(null);
  }
}
