package com.kosaf.core.api.role.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoleHierarchy {

  private String roleId;
  private String parentRoleId;
}
