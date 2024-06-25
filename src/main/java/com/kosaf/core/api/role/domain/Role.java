package com.kosaf.core.api.role.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Role {

  @NonNull
  private String roleId;// 롤 아이디
  @NonNull
  private String roleNm;// 롤명
  private String expln;// 설명
  private LocalDateTime regDt; // 등록일시
  @NonNull
  private String rgtrSysId;// 등록자 시스템 아이디
  private LocalDateTime mdfcnDt;// 수정일시
  @NonNull
  private String mdfrSysId;// 수정자 시스템 아이디

}
