package com.kosaf.core.api.menu.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuListParam {

  private Long menuNo;

  @Builder
  public MenuListParam(Long menuNo) {

    this.menuNo = (menuNo == null) ? 0 : menuNo;
  }
}
