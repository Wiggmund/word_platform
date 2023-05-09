package com.example.word_platform.service;

import com.example.word_platform.dto.role.RoleCreateDto;
import com.example.word_platform.model.Role;

public interface RoleService {
  Role getRoleByName(String name);
  Role getDefaultRole();
  Role createRole(RoleCreateDto dto);
}
