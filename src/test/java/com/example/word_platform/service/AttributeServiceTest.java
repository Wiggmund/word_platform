package com.example.word_platform.service;

import static com.example.word_platform.TestConstants.ATTRIBUTE_BASE_TYPE;
import static com.example.word_platform.TestConstants.ATTRIBUTE_CUSTOM_TYPE;
import static com.example.word_platform.TestConstants.BASE_ATTRIBUTE_NAME_1;
import static com.example.word_platform.TestConstants.CUSTOM_ATTRIBUTE_NAME_1;
import static com.example.word_platform.TestConstants.EXISTING_ID_LONG;
import static com.example.word_platform.TestConstants.NOT_EXISTING_ID_LONG;
import static com.example.word_platform.TestConstants.WORD_BASE_ATTRIBUTE_VALUE_1;
import static com.example.word_platform.TestConstants.WORD_CUSTOM_ATTRIBUTE_VALUE_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.word_platform.dto.attribute.AttributeCreateDto;
import com.example.word_platform.dto.word.WordsAttributesCreateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.Attribute;
import com.example.word_platform.repository.AttributeRepo;
import com.example.word_platform.service.impl.AttributeServiceImpl;
import com.example.word_platform.shared.DuplicationCheckService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {
  private Attribute baseAttribute;
  private Attribute customAttribute;
  private Attribute baseAttributeWithoutId;
  private Attribute customAttributeWithoutId;
  private AttributeCreateDto baseAttributeCreateDto;
  private AttributeCreateDto customAttributeCreateDto;
  private List<Attribute> attributes;
  private List<Attribute> onlyBaseTypes;
  private List<Attribute> onlyCustomTypes;
  private List<Attribute> attributesWithoutId;
  private List<Attribute> customAttributesWithoutId;
  private List<AttributeCreateDto> attributeCreateDtos;
  private List<WordsAttributesCreateDto> wordsAttributesCreateDtosAllTypes;
  private List<WordsAttributesCreateDto> wordsAttributesCreateDtosCustomTypes;
  private List<String> wordsAttributesCreateDtoNames;
  private List<String> customAttributeNames;
  @Mock
  private AttributeRepo attributeRepo;

  @Mock
  private DuplicationCheckService duplicationCheckService;

  @InjectMocks
  private AttributeServiceImpl underTest;

  @BeforeEach
  void setUp() {
    baseAttribute = Attribute.builder()
        .id(1L)
        .name(BASE_ATTRIBUTE_NAME_1)
        .type(ATTRIBUTE_BASE_TYPE)
        .build();
    customAttribute = Attribute.builder()
        .id(2L)
        .name(CUSTOM_ATTRIBUTE_NAME_1)
        .type(ATTRIBUTE_CUSTOM_TYPE)
        .build();

    baseAttributeWithoutId = Attribute.builder()
        .name(baseAttribute.getName())
        .type(baseAttribute.getType())
        .build();
    customAttributeWithoutId = Attribute.builder()
        .name(customAttribute.getName())
        .type(customAttribute.getType())
        .build();

    baseAttributeCreateDto = AttributeCreateDto.builder()
        .name(baseAttribute.getName())
        .type(baseAttribute.getType())
        .build();
    customAttributeCreateDto = AttributeCreateDto.builder()
        .name(customAttribute.getName())
        .type(customAttribute.getType())
        .build();

    wordsAttributesCreateDtosAllTypes = List.of(
        WordsAttributesCreateDto.builder()
            .name(baseAttribute.getName())
            .type(baseAttribute.getType())
            .value(WORD_BASE_ATTRIBUTE_VALUE_1)
            .build(),
        WordsAttributesCreateDto.builder()
            .name(customAttribute.getName())
            .type(customAttribute.getType())
            .value(WORD_CUSTOM_ATTRIBUTE_VALUE_1)
            .build()
    );

    attributes = List.of(baseAttribute, customAttribute);
    attributesWithoutId = List.of(baseAttributeWithoutId, customAttributeWithoutId);
    attributeCreateDtos = List.of(baseAttributeCreateDto, customAttributeCreateDto);

    onlyBaseTypes = attributes.stream()
        .filter(item -> item.getType().equals(ATTRIBUTE_BASE_TYPE))
        // We use toCollection() instead of toList() to get mutable list
        .collect(Collectors.toCollection(ArrayList::new));
    onlyCustomTypes = attributes.stream()
        .filter(item -> item.getType().equals(ATTRIBUTE_CUSTOM_TYPE))
        .toList();

    customAttributesWithoutId = attributesWithoutId.stream()
        .filter(item -> item.getType().equals(ATTRIBUTE_CUSTOM_TYPE))
        .toList();

    wordsAttributesCreateDtosCustomTypes = wordsAttributesCreateDtosAllTypes.stream()
        .filter(item -> item.type().equals(ATTRIBUTE_CUSTOM_TYPE))
        .toList();

    wordsAttributesCreateDtoNames = wordsAttributesCreateDtosAllTypes.stream()
        .map(WordsAttributesCreateDto::name)
        .toList();
    customAttributeNames = wordsAttributesCreateDtosCustomTypes.stream()
        .map(WordsAttributesCreateDto::name)
        .toList();
  }

  @Test
  void getAllAttributes_ShouldReturnListOfAttributes() {
    //given
    when(attributeRepo.findAll()).thenReturn(attributes);

    //when
    List<Attribute> actual = underTest.getAllAttributes();

    //then
    assertThat(actual).isNotNull().hasSize(attributes.size());
  }

  @Test
  void getAttributeById_ShouldReturnAttribute() {
    //given
    when(attributeRepo.findById(EXISTING_ID_LONG)).thenReturn(Optional.of(baseAttribute));

    //when
    Attribute actual = underTest.getAttributeById(EXISTING_ID_LONG);

    //then
    assertThat(actual).isEqualTo(baseAttribute);
  }

  @Test
  void getAttributeByNotExistingId_ShouldThrowException() {
    //given
    when(attributeRepo.findById(NOT_EXISTING_ID_LONG)).thenReturn(Optional.empty());

    //when
    //then
    assertThatThrownBy(() -> underTest.getAttributeById(NOT_EXISTING_ID_LONG))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void createAttribute_ShouldCreateNewAttribute() {
    //given
    when(attributeRepo.save(baseAttributeWithoutId)).thenReturn(baseAttribute);

    //when
    Attribute actual = underTest.createAttribute(baseAttributeCreateDto);

    //then
    verify(duplicationCheckService).checkAttributeForName(baseAttributeCreateDto.name());
    assertThat(actual).isNotNull().isEqualTo(baseAttribute);
  }

  @Test
  void createBaseAttribute_ShouldCreateNewBaseAttribute() {
    //given
    when(attributeRepo.save(baseAttributeWithoutId)).thenReturn(baseAttribute);

    //when
    Attribute actual = underTest.createBaseAttribute(baseAttributeCreateDto);

    //then
    verify(duplicationCheckService).checkAttributeForName(baseAttributeCreateDto.name());
    assertThat(actual).isNotNull().isEqualTo(baseAttribute);
  }

//  @Test
//  @Disabled
//  void createBaseAttributeIgnoringCaseForType_ShouldCreateNewBaseAttribute() {
//    //given
//    AttributeCreateDto baseAttributeCreateDtoTypeRandomCase = AttributeCreateDto.builder()
//        .name(baseAttribute.getName())
//        .type(stringToRandomCase(baseAttribute.getType()))
//        .build();
//
//    when(attributeRepo.save(baseAttributeWithoutId)).thenReturn(baseAttribute);
//
//    //when
//    Attribute actual =
//        underTest.createBaseAttribute(baseAttributeCreateDtoTypeRandomCase);
//
//    //then
//    verify(duplicationCheckService).checkAttributeForName(baseAttributeCreateDto.name());
//    assertThat(actual).isNotNull().isEqualTo(baseAttribute);
//  }

  @Test
  void createBaseAttributeActuallyNotBaseType_ShouldThrowException() {
    //given
    //when
    //then
    assertThatThrownBy(() -> underTest.createBaseAttribute(customAttributeCreateDto))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void createManyAttributes_ShouldCreateOneOrMoreAttributes() {
    //given
    List<String> attributeCreateDtoNames = attributeCreateDtos.stream()
        .map(AttributeCreateDto::name)
        .toList();

    when(attributeRepo.saveAll(attributesWithoutId)).thenReturn(attributes);

    //when
    List<Attribute> actual = underTest.createManyAttributes(attributeCreateDtos);

    //then
    verify(duplicationCheckService).checkAttributesForName(attributeCreateDtoNames);
    assertThat(actual).isNotNull().hasSize(attributeCreateDtos.size());
  }

  @Test
  void getAttributesFromOrCreateAllAttributesAlreadyExist_ShouldReturnListOfAttributes() {
    //given
    when(attributeRepo.findAllByNameIgnoreCaseIn(wordsAttributesCreateDtoNames))
        .thenReturn(attributes);

    //when
    List<Attribute> actual = underTest.getAttributesFromOrCreate(wordsAttributesCreateDtosAllTypes);

    //then
    verify(attributeRepo, never()).save(any(Attribute.class));
    verifyNoInteractions(duplicationCheckService);
    assertThat(actual).isNotNull().hasSize(attributes.size());
  }

  @Test
  @DisplayName("getAttributesFromOrCreateBaseExistCustomCreate" +
      "_ShouldCreateCustomTypesAndReturnListOfAllTypesAttributes")
  void getAttributesFromOrCreateBaseExistCustomCreate() {
    //given
    when(attributeRepo.findAllByNameIgnoreCaseIn(wordsAttributesCreateDtoNames))
        .thenReturn(onlyBaseTypes);
    when(attributeRepo.saveAll(customAttributesWithoutId))
        .thenReturn(onlyCustomTypes);

    //when
    List<Attribute> actual = underTest.getAttributesFromOrCreate(wordsAttributesCreateDtosAllTypes);

    //then
    verify(duplicationCheckService).checkAttributesForName(customAttributeNames);
    verify(attributeRepo).saveAll(customAttributesWithoutId);
    assertThat(actual).isNotNull().hasSize(attributes.size());
  }

  @Test
  void getAttributesFromOrCreateNotExistingBaseAttribute_ShouldThrowException() {
    //given
    when(attributeRepo.findAllByNameIgnoreCaseIn(wordsAttributesCreateDtoNames))
        .thenReturn(Collections.emptyList());

    //when
    //then
    assertThatThrownBy(() -> underTest.getAttributesFromOrCreate(wordsAttributesCreateDtosAllTypes))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void getAttributesFromOrCreateOnlyNotExistingCustomTypes_ShouldCreateCustomTypesAndReturnListOfThem() {
    //given
    when(attributeRepo.findAllByNameIgnoreCaseIn(customAttributeNames)).thenReturn(
        new ArrayList<>());
    when(attributeRepo.saveAll(customAttributesWithoutId))
        .thenReturn(onlyCustomTypes);

    //when
    List<Attribute> actual =
        underTest.getAttributesFromOrCreate(wordsAttributesCreateDtosCustomTypes);

    //then
    verify(duplicationCheckService).checkAttributesForName(customAttributeNames);
    verify(attributeRepo).saveAll(customAttributesWithoutId);
    assertThat(actual).isNotNull().hasSize(wordsAttributesCreateDtosCustomTypes.size());
  }
}