package com.example.word_platform.model;

import com.example.word_platform.model.word.Word;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "stats")
@Data
@NoArgsConstructor
public class Stats {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDate date;
  private Boolean correct;

  public Stats(LocalDate date, Boolean correct) {
    this.date = date;
    this.correct = correct;
  }

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "wordlist_id")
  private Wordlist wordlist;

  @ManyToOne
  @JoinColumn(name = "word_id")
  private Word word;

  @ManyToOne
  @JoinColumn(name = "question_id")
  private Question question;
}
