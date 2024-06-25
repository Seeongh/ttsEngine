package com.kosaf.core.api.menu.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleListParam {

  private final static int PAGE = 1;
  private final static int DEFAULT_PAGE_SCALE = 10;

  private Integer page = PAGE;
  private Integer pageScale = DEFAULT_PAGE_SCALE;

  private String searchKeyword;

  @Builder
  public RoleListParam(Integer page, Integer pageScale, String searchKeyword) {
    this.page = (page == null) ? PAGE : page;
    this.pageScale = (pageScale == null) ? DEFAULT_PAGE_SCALE : pageScale;
    this.searchKeyword = searchKeyword;
  }
}
