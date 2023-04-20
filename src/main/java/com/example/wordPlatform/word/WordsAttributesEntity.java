package com.example.wordPlatform.word;

import com.example.wordPlatform.attribute.AttributeEntity;
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
public class WordsAttributesEntity {
  @Id
  @ManyToOne
  @JoinColumn(name = "word_id")
  private WordEntity word;

  @Id
  @ManyToOne
  @JoinColumn(name = "attribute_id")
  private AttributeEntity attribute;

  private String value;
}
