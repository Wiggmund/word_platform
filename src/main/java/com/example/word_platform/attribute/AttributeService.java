package com.example.word_platform.attribute;

import com.example.word_platform.attribute.dto.AttributeCreateDto;
import com.example.word_platform.exception.IllegalAttributesException;
import com.example.word_platform.exception.not_found.AttributeNotFoundException;
import com.example.word_platform.shared.DuplicationCheckService;
import com.example.word_platform.word.dto.WordsAttributesCreateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AttributeService {
  private final AttributeRepo attributeRepo;
  private final DuplicationCheckService duplicationCheckService;

  public List<AttributeEntity> getAllAttributes() {
    return attributeRepo.findAll();
  }

  public AttributeEntity getAttributeByName(String name) {
    return attributeRepo.findByName(name)
            .orElseThrow(AttributeNotFoundException::new);
  }

  public AttributeEntity createAttribute(AttributeCreateDto dto) {
    duplicationCheckService.checkAttributeForName(dto.name());
    AttributeEntity newAttribute = new AttributeEntity(dto.name(), dto.type());
    return attributeRepo.save(newAttribute);
  }

  public AttributeEntity createBaseAttribute(AttributeCreateDto dto) {
    if (!dto.type().equalsIgnoreCase("base"))
      throw new IllegalArgumentException("Can't create attribute for types other than [base]");

    return createAttribute(dto);
  }

  public List<AttributeEntity> createManyAttributes(List<AttributeCreateDto> dtos) {
    duplicationCheckService.checkAttributesForName(
            dtos.stream().map(AttributeCreateDto::name).toList()
    );

    List<AttributeEntity> newAttributes = dtos.stream()
            .map(item -> new AttributeEntity(item.name(), item.type()))
            .toList();

    return attributeRepo.saveAll(newAttributes);
  }

  public List<AttributeEntity> getAttributesFromOrCreate(List<WordsAttributesCreateDto> wordsAttributesCreateDtos) {
    List<AttributeEntity> fetchedAttributes = attributeRepo.findAllByNameIn(
            wordsAttributesCreateDtos.stream().map(WordsAttributesCreateDto::name).toList()
    );

    if (fetchedAttributes.size() == wordsAttributesCreateDtos.size())
      return fetchedAttributes;

    if (fetchedAttributes.size() < wordsAttributesCreateDtos.size()) {
      checkBaseAttributesPresence(wordsAttributesCreateDtos, fetchedAttributes);

      fetchedAttributes.addAll(
              createCustomAttributes(wordsAttributesCreateDtos, fetchedAttributes)
      );
    }

    return fetchedAttributes;
  }

  public List<AttributeEntity> getAttributesFromOrThrow(List<WordsAttributesCreateDto> wordsAttributesCreateDtos) {
    List<AttributeEntity> fetchedAttributes = attributeRepo.findAllByNameIn(
            wordsAttributesCreateDtos.stream().map(WordsAttributesCreateDto::name).toList()
    );

    if (fetchedAttributes.size() < wordsAttributesCreateDtos.size()) {
      checkBaseAttributesPresence(wordsAttributesCreateDtos, fetchedAttributes);

      throw new IllegalArgumentException("Illegal to create new custom attributes");
    }

    return fetchedAttributes;
  }

  private void checkBaseAttributesPresence(
          List<WordsAttributesCreateDto> wordsAttributesCreateDtos,
          List<AttributeEntity> fetchedAttributes
  ) {
    List<String> dtoBaseAttributeNames = wordsAttributesCreateDtos.stream()
            .filter(dto -> dto.type().equalsIgnoreCase("base"))
            .map(WordsAttributesCreateDto::name)
            .toList();

    List<String> fetchedAttributeNames = fetchedAttributes.stream()
            .filter(attribute -> attribute.getType().equalsIgnoreCase("base"))
            .map(AttributeEntity::getName)
            .toList();

    List<String> nonExistentBaseAttributes = dtoBaseAttributeNames.stream()
            .filter(dtoBaseAttributeName -> !fetchedAttributeNames.contains(dtoBaseAttributeName))
            .toList();

    if(!nonExistentBaseAttributes.isEmpty())
      throw new IllegalAttributesException(
              "Following base attributes don't exist",
              nonExistentBaseAttributes
      );
  }
  private List<AttributeEntity> createCustomAttributes(
          List<WordsAttributesCreateDto> wordsAttributesCreateDtos,
          List<AttributeEntity> fetchedAttributes
  ) {
    List<WordsAttributesCreateDto> customAttributeDtos = wordsAttributesCreateDtos.stream()
            .filter(dto -> dto.type().equalsIgnoreCase("custom"))
            .toList();

    List<String> customFetchedAttributeNames = fetchedAttributes.stream()
            .filter(attribute -> attribute.getType().equalsIgnoreCase("custom"))
            .map(AttributeEntity::getName)
            .toList();

    List<AttributeCreateDto> customAttributesDtosToCreate = customAttributeDtos.stream()
            .filter(dto -> !customFetchedAttributeNames.contains(dto.name()))
            .map(dto -> new AttributeCreateDto(dto.name(), dto.type()))
            .toList();

    return createManyAttributes(customAttributesDtosToCreate);
  }
}
