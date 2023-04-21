package com.example.word_platform.wordlist;

import com.example.word_platform.attribute.AttributeEntity;
import com.example.word_platform.user.UserEntity;
import com.example.word_platform.word.WordEntity;
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
public class WordlistEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;

  @OneToMany(mappedBy = "wordlist", cascade = CascadeType.ALL)
  private List<WordEntity> words = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
          name = "wordlists_attributes",
          joinColumns = @JoinColumn(name = "wordlist_id"),
          inverseJoinColumns = @JoinColumn(name = "attribute_id")
  )
  private List<AttributeEntity> attributes = new ArrayList<>();

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

  public void addAttribute(AttributeEntity attribute) {
    attributes.add(attribute);
    attribute.getWordlists().add(this);
  }

  public void removeAttribute(AttributeEntity attribute) {
    attributes.remove(attribute);
    attribute.getWordlists().remove(this);
  }
}
