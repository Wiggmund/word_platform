package com.example.word_platform.word;

import com.example.word_platform.attribute.AttributeEntity;
import com.example.word_platform.user.UserEntity;
import com.example.word_platform.wordlist.WordlistEntity;
import jakarta.persistence.CascadeType;
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
@Table(name = "words")
@Data
@NoArgsConstructor
public class WordEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String value;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "wordlist_id")
  private WordlistEntity wordlist;

  @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WordsAttributesEntity> attributes = new ArrayList<>();

  public WordEntity(String value) {
    this.value = value;
  }

  public WordsAttributesEntity addAttribute(AttributeEntity attribute, String value) {
    WordsAttributesEntity wordAttribute = new WordsAttributesEntity(this, attribute, value);
    attributes.add(wordAttribute);
    attribute.getWords().add(wordAttribute);
    return wordAttribute;
  }

  public void removeAttribute(AttributeEntity attribute, String value) {
    WordsAttributesEntity wordAttribute = new WordsAttributesEntity(this, attribute, value);
    attribute.getWords().remove(wordAttribute);
    attributes.remove(wordAttribute);
    wordAttribute.setWord(null);
    wordAttribute.setAttribute(null);
  }
}
