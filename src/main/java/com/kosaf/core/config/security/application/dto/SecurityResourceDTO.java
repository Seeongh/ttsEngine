package com.kosaf.core.config.security.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SecurityResourceDTO {

  // 인가 처리시 resource 별 접근 허용 role 목록 조회를 위한 DTO
  private String pattern;
  private String roles;
}
