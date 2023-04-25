package com.example.word_platform.controller;

import com.example.word_platform.model.Attribute;
import com.example.word_platform.service.AttributeService;
import com.example.word_platform.dto.attribute.AttributeCreateDto;
import com.example.word_platform.dto.attribute.AttributeResponseDto;
import com.example.word_platform.shared.EntityConverter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/attributes")
@AllArgsConstructor
public class AttributeController {
  private final AttributeService attributeService;
  private final EntityConverter entityConverter;

  @GetMapping
  public ResponseEntity<List<AttributeResponseDto>> getAllAttributes() {
    List<AttributeResponseDto> fetchedAttrs = attributeService.getAllAttributes().stream()
            .map(entityConverter::entityToDto)
            .toList();

    return ResponseEntity.ok(fetchedAttrs);
  }

  @PostMapping("/base")
  public ResponseEntity<AttributeResponseDto> createBaseAttribute(@RequestBody AttributeCreateDto dto) {
    Attribute createdBaseAttribute = attributeService.createBaseAttribute(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(entityConverter.entityToDto(createdBaseAttribute));
  }
}
