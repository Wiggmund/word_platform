package com.example.word_platform.service.impl;

import com.example.word_platform.dto.role.RoleCreateDto;
import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.Role;
import com.example.word_platform.repository.RoleRepo;
import com.example.word_platform.service.RoleService;
import com.example.word_platform.shared.DuplicationCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
  private static final String ROLE_NOT_FOUND_BY_NAME = "Role not found by name [%s]";
  private static final String DEFAULT_ROLE = "USER";
  private final RoleRepo roleRepo;
  private final DuplicationCheckService duplicationCheckService;

  @Override
  public Role getRoleByName(String name) {
    log.debug("Getting role by name {}", name);
    return roleRepo.findByNameIgnoreCase(name).orElseThrow(() ->
        new ResourceNotFoundException(String.format(ROLE_NOT_FOUND_BY_NAME, name)));
  }

  @Override
  public Role getDefaultRole() {
    log.debug("Getting default role {}", DEFAULT_ROLE);
    return roleRepo.findByNameIgnoreCase(DEFAULT_ROLE).orElseGet(() -> {
      log.debug("Default role {} doesn't exist", DEFAULT_ROLE);
      log.debug("Creating default role {}...", DEFAULT_ROLE);

      return createRole(
          RoleCreateDto.builder()
              .name(DEFAULT_ROLE)
              .build()
      );
    });
  }

  @Override
  public Role createRole(RoleCreateDto dto) {
    log.debug("Creating role...");
    duplicationCheckService.checkRoleForName(dto.name());

    Role newRole = Role.builder()
        .name(dto.name().toUpperCase())
        .build();

    Role savedRole = roleRepo.save(newRole);
    log.debug("Role {} was created", savedRole);
    return savedRole;
  }
}
