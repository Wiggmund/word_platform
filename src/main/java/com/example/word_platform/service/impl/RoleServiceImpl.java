package com.example.word_platform.service.impl;

import com.example.word_platform.exception.ResourceNotFoundException;
import com.example.word_platform.model.Role;
import com.example.word_platform.repository.RoleRepo;
import com.example.word_platform.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
  private static final String ROLE_NOT_FOUND_BY_NAME = "Role not found by name [%s]";
  private final RoleRepo roleRepo;
  @Override
  public Role getRoleByName(String name) {
    log.debug("Getting role by name {}", name);
    return roleRepo.findByName(name).orElseThrow(() ->
        new ResourceNotFoundException(String.format(ROLE_NOT_FOUND_BY_NAME, name)));
  }
}
