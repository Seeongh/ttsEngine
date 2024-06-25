package com.kosaf.core.api.role.application.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleDetailDTO {

  private String roleId;

  private String roleNm;
  private String expln;
  private LocalDateTime regDt;
  private String rgtrSysId;
  private LocalDateTime mdfcnDt;
  private String mdfrSysId;

  private String authorId;
  private String pattern;

  // List<Author> authors;
}
