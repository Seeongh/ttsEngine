package com.kosaf.core.common;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageResult<T> {

  private final static Integer PAGE = 1;
  private final static Integer DEFAULT_PAGE_SCALE = 10;

  private Integer page;
  private Integer pageScale;

  private Integer totalCount;

  private List<T> items = new ArrayList<>();

  @Builder
  public PageResult(Integer page, Integer pageScale, Integer totalCount, List<T> items) {

    this.page = (page == null) ? PAGE : page;
    this.pageScale = (pageScale == null) ? DEFAULT_PAGE_SCALE : pageScale;
    this.totalCount = totalCount;
    this.items = items;
  }

}
