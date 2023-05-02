package com.example.word_platform.service;

import com.example.word_platform.dto.attribute.AttributeCreateDto;
import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.Attribute;
import java.util.List;

/**
 * This interface contains all needed methods to manage attributes.
 */
public interface AttributeService {
  /**
   * The method returns list of entities {@code List<Attribute>} of all attributes.
   *
   * @return new {@code List<Attribute>}.
   */
  List<Attribute> getAllAttributes();

  /**
   * The method returns entity {@code Attribute} of attribute fetched by id.
   *
   * @param attributeId put attribute id.
   * @return new {@code Attribute}.
   * @throws ResourceNotFoundException if attribute not exists.
   */
  Attribute getAttributeById(Long attributeId);

  /**
   * The method returns entity {@code Attribute} of attribute if it was successfully created.
   *
   * @param dto put dto {@code AttributeCreateDto}.
   * @return new {@code Attribute}.
   */
  Attribute createAttribute(AttributeCreateDto dto);

  Attribute createBaseAttribute(AttributeCreateDto dto);

  List<Attribute> createManyAttributes(List<AttributeCreateDto> dtos);

  List<Attribute> getAttributesFromOrCreate(
      List<WordsAttributesCreateDto> wordsAttributesCreateDtos);

  /**
   * The method creates new {@code Attribute} entity and returns it.
   *
   * @param attributeId attribute id
   * @return new {@code Attribute}
   * @throws ResourceNotFoundException if attribute not exists
   */
  Attribute removeAttributeById(Long attributeId);
}
