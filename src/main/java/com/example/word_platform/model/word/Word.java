package com.example.word_platform.model.word;

import com.example.word_platform.model.Attribute;
import com.example.word_platform.model.User;
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
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "words")
@Data
@NoArgsConstructor
public class Word {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String value;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "wordlist_id")
  private Wordlist wordlist;

  @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WordsAttributes> attributes = new ArrayList<>();

  public Word(String value) {
    this.value = value;
  }

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
