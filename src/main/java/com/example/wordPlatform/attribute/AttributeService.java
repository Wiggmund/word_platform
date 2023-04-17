package com.example.wordPlatform.attribute;

import com.example.wordPlatform.attribute.dto.AttributeCreateDto;
import com.example.wordPlatform.exception.notFound.AttributeNotFoundException;
import com.example.wordPlatform.shared.DuplicationCheckService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

  public List<AttributeEntity> getAttributesIfExistOrCreate(Map<String, String> receivedAttributes) {
    List<String> receivedAttributeNames = receivedAttributes.keySet().stream().toList();
    List<AttributeEntity> foundedAttributes = attributeRepo.findAllByNameIn(receivedAttributeNames);

    if (receivedAttributes.size() != foundedAttributes.size()) {
      List<String> foundedAttributeNames = foundedAttributes.stream().map(AttributeEntity::getName).toList();

      long foundedBaseTypeCount = foundedAttributes.stream()
              .filter(item -> item.getType().equalsIgnoreCase("base"))
              .count();
      long requiredBaseTypeCount = receivedAttributes.entrySet().stream()
              .filter(item ->  item.getValue().equalsIgnoreCase("base"))
              .count();

      if (foundedBaseTypeCount != requiredBaseTypeCount)
        throw new AttributeNotFoundException();

      List<AttributeCreateDto> newCustomAttributeDtos = receivedAttributeNames.stream()
              .filter(name -> !foundedAttributeNames.contains(name))
              .map(name -> new AttributeCreateDto(name, "custom"))
              .toList();

      List<AttributeEntity> createdCustomAttributes = createManyAttributes(newCustomAttributeDtos);
      foundedAttributes.addAll(createdCustomAttributes);
    }

    return foundedAttributes;
  }
}
