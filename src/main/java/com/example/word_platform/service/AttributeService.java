package com.example.word_platform.service;

import com.example.word_platform.dto.attribute.AttributeCreateDto;
import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.repository.AttributeRepo;
import com.example.word_platform.shared.DuplicationCheckService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AttributeService {
  private static final String ATTRIBUTE_NOT_FOUND_BY_ID = "Attribute not found by id [%s]";
  private static final String BASE_ATTRIBUTES_NOT_FOUND = "Base attributes [%s] don't exist";
  private static final String ILLEGAL_CREATE_NOT_BASE_TYPE =
      "Can't create attribute for types other than [base]";

  private final AttributeRepo attributeRepo;
  private final DuplicationCheckService duplicationCheckService;

  public List<Attribute> getAllAttributes() {
    log.debug("Getting all attributes");
    return attributeRepo.findAll();
  }

  public Attribute getAttributeById(Long attributeId) {
    log.debug("Getting attribute by id {}", attributeId);
    return attributeRepo.findById(attributeId).orElseThrow(() ->
        new ResourceNotFoundException(String.format(ATTRIBUTE_NOT_FOUND_BY_ID, attributeId)));
  }

  public Attribute createAttribute(AttributeCreateDto dto) {
    log.debug("Creating attribute...");
    duplicationCheckService.checkAttributeForName(dto.name());

    Attribute newAttribute = Attribute.builder()
        .name(dto.name())
        .type(dto.type())
        .build();

    log.debug("Attribute was created: {}", newAttribute);
    return attributeRepo.save(newAttribute);
  }

  public Attribute createBaseAttribute(AttributeCreateDto dto) {
    log.debug("Creating base type attribute...");
    if (!dto.type().equalsIgnoreCase("base")) {
      throw new IllegalArgumentException(ILLEGAL_CREATE_NOT_BASE_TYPE);
    }

    Attribute createdBaseAttribute = createAttribute(dto);
    log.debug("Base attribute was created: {}", createdBaseAttribute);
    return createdBaseAttribute;
  }

  public List<Attribute> createManyAttributes(List<AttributeCreateDto> dtos) {
    log.debug("Creating many attributes...");
    duplicationCheckService.checkAttributesForName(
        dtos.stream().map(AttributeCreateDto::name).toList()
    );

    List<Attribute> newAttributes = dtos.stream()
        .map(item -> Attribute.builder()
            .name(item.name())
            .type(item.type())
            .build())
        .toList();

    log.debug("{} new attributes was created", newAttributes.size());
    return attributeRepo.saveAll(newAttributes);
  }

  public List<Attribute> getAttributesFromOrCreate(
      List<WordsAttributesCreateDto> wordsAttributesCreateDtos) {
    log.debug("Getting {} attributes or create if some doesn't exist", wordsAttributesCreateDtos.size());

    List<Attribute> fetchedAttributes = attributeRepo.findAllByNameIn(
        wordsAttributesCreateDtos.stream().map(WordsAttributesCreateDto::name).toList()
    );
    log.debug("Fetched {} attributes", fetchedAttributes.size());

    if (fetchedAttributes.size() < wordsAttributesCreateDtos.size()) {
      checkBaseAttributesPresence(wordsAttributesCreateDtos, fetchedAttributes);

      List<Attribute> createdCustomAttributes =
          createCustomAttributes(wordsAttributesCreateDtos, fetchedAttributes);

      log.debug("Created {} custom attributes", createdCustomAttributes.size());
      fetchedAttributes.addAll(createdCustomAttributes);
    }

    return fetchedAttributes;
  }

  private void checkBaseAttributesPresence(
      List<WordsAttributesCreateDto> wordsAttributesCreateDtos,
      List<Attribute> fetchedAttributes
  ) {
    log.debug("Checking base attributes existence...");
    List<String> dtoBaseAttributeNames = wordsAttributesCreateDtos.stream()
        .filter(dto -> dto.type().equalsIgnoreCase("base"))
        .map(WordsAttributesCreateDto::name)
        .toList();

    List<String> fetchedAttributeNames = fetchedAttributes.stream()
        .filter(attribute -> attribute.getType().equalsIgnoreCase("base"))
        .map(Attribute::getName)
        .toList();

    List<String> nonExistentBaseAttributes = dtoBaseAttributeNames.stream()
        .filter(dtoBaseAttributeName -> !fetchedAttributeNames.contains(dtoBaseAttributeName))
        .toList();

    if (!nonExistentBaseAttributes.isEmpty()) {
      throw new ResourceNotFoundException(String.format(
          BASE_ATTRIBUTES_NOT_FOUND,
          String.join(", ", nonExistentBaseAttributes)
      ));
    }
    log.debug("All base attributes exist");
  }

  private List<Attribute> createCustomAttributes(
      List<WordsAttributesCreateDto> wordsAttributesCreateDtos,
      List<Attribute> fetchedAttributes
  ) {
    log.debug("Creating custom attributes...");
    List<WordsAttributesCreateDto> customAttributeDtos = wordsAttributesCreateDtos.stream()
        .filter(dto -> dto.type().equalsIgnoreCase("custom"))
        .toList();

    List<String> customFetchedAttributeNames = fetchedAttributes.stream()
        .filter(attribute -> attribute.getType().equalsIgnoreCase("custom"))
        .map(Attribute::getName)
        .toList();

    List<AttributeCreateDto> customAttributesDtosToCreate = customAttributeDtos.stream()
        .filter(dto -> !customFetchedAttributeNames.contains(dto.name()))
        .map(dto -> new AttributeCreateDto(dto.name(), dto.type()))
        .toList();

    return createManyAttributes(customAttributesDtosToCreate);
  }
}
