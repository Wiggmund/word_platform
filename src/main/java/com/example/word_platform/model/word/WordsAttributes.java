package com.example.word_platform.model.word;

import com.example.word_platform.model.Attribute;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "words-attributes")
@IdClass(WordsAttributesIdClass.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordsAttributes {
  @Id
  @ManyToOne
  @JoinColumn(name = "word_id")
  private Word word;

  @Id
  @ManyToOne
  @JoinColumn(name = "attribute_id")
  private Attribute attribute;

  private String value;
}
