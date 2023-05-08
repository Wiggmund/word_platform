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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String text;
  private String type;

  @OneToMany(mappedBy = "question")
  @ToString.Exclude
  @Builder.Default
  private List<Stats> statsRecords = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "attribute_id")
  @ToString.Exclude
  private Attribute answer;

  @ManyToOne
  @JoinColumn(name = "wordlist_id")
  @ToString.Exclude
  private Wordlist wordlist;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private AppUser appUser;
}