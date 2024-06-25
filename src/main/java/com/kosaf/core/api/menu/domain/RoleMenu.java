package com.kosaf.core.api.menu.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleMenu {

  private String roleId;
  private Long menuNo;

  private LocalDateTime regDt;
  private Integer rgtrSysId;

  @Builder
  public RoleMenu(String roleId, Long menuNo, LocalDateTime regDt, Integer rgtrSysId) {
    this.roleId = roleId;
    this.menuNo = menuNo;
    this.regDt = regDt;
    this.rgtrSysId = rgtrSysId;
  }

  public void create(Integer sysId) {
    this.regDt = LocalDateTime.now();
    this.rgtrSysId = sysId;
  }
}
