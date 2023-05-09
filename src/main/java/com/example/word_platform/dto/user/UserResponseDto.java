package com.example.word_platform.dto.user;

import com.example.word_platform.dto.role.RoleResponseDto;
import java.util.List;

public record UserResponseDto(
    Long id,
    String username,
    String email,
    List<RoleResponseDto> roles
) {
}
