package com.example.wordPlatform.wordlist;

import com.example.wordPlatform.user.UserEntity;
import com.example.wordPlatform.word.WordEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class WordlistEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;

  @OneToMany(mappedBy = "wordlist")
  private List<WordEntity> words = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  public WordlistEntity(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public void addWord(WordEntity word) {
    words.add(word);
    word.setWordlist(this);
  }

  public void removeWord(WordEntity word) {
    words.remove(word);
    word.setWordlist(null);
  }
}
