package com.kosaf.core.api.menu.application.dto;

import java.time.LocalDateTime;
import com.kosaf.core.api.menu.domain.RoleMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenusCreateDTO {
  private String roleId;
  private Long menuNo;

  private LocalDateTime regDt;
  private Integer rgtrSysId;

  public RoleMenu toEntity() {

    return RoleMenu
        .builder()
        .roleId(roleId)
        .menuNo(menuNo)
        .regDt(regDt)
        .rgtrSysId(rgtrSysId)
        .build();

  }
}
