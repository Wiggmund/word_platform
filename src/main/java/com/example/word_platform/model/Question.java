package com.example.word_platform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String text;
  private String type;

  @OneToMany(mappedBy = "question")
  private List<Stats> statsRecords = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "attribute_id")
  private Attribute answer;

  @ManyToOne
  @JoinColumn(name = "wordlist_id")
  private Wordlist wordlist;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  public Question(String text, String type) {
    this.text = text;
    this.type = type;
  }
}