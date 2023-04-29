package com.example.word_platform.model;

import com.example.word_platform.model.word.Word;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stats {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "testing_date")
  private LocalDate testingDate;

  private Boolean correct;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private User user;

  @ManyToOne
  @JoinColumn(name = "wordlist_id")
  @ToString.Exclude
  private Wordlist wordlist;

  @ManyToOne
  @JoinColumn(name = "word_id")
  @ToString.Exclude
  private Word word;

  @ManyToOne
  @JoinColumn(name = "question_id")
  @ToString.Exclude
  private Question question;
}
