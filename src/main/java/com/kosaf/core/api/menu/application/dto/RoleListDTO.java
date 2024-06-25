package com.kosaf.core.api.menu.application.dto;

import java.time.LocalDateTime;
import com.kosaf.core.api.role.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoleListDTO {

  private int rowNum;// 순번
  private String roleId;// 롤 아이디
  private String roleNm;// 롤명
  private String expln;// 설명
  private LocalDateTime regDt; // 등록일시
  private String rgtrSysId;// 등록자 시스템 아이디
  private LocalDateTime mdfcnDt;// 수정일시
  private String mdfrSysId;// 수정자 시스템 아이디

  public static RoleListDTO of(Role role) {
    return RoleListDTO
        .builder()
        .roleId(role.getRoleId())
        .roleNm(role.getRoleNm())
        .expln(role.getExpln())
        .regDt(role.getRegDt())
        .rgtrSysId(role.getRgtrSysId())
        .mdfcnDt(role.getMdfcnDt())
        .mdfrSysId(role.getMdfrSysId())
        .build();
  }
}
