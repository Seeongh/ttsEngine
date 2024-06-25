package com.kosaf.core.api.bbs.application.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.bbs.value.DelYn;
import com.kosaf.core.api.bbs.value.SearchCondition;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BbsListParam {

  private final static int PAGE = 1;
  private final static int DEFAULT_PAGE_SCALE = 10;

  /**
   * 조회페이지
   */
  private Integer page = PAGE;

  /**
   * 페이지당 게시물수
   */
  private Integer pageScale = DEFAULT_PAGE_SCALE;

  @Enumerated(EnumType.STRING)
  private DelYn delYn = DelYn.N;

  /**
   * 검색 기간
   */
  private String searchStartDt;
  private String searchEndDt;

  /**
   * 검색 조건
   */
  @Enumerated(EnumType.STRING)
  private SearchCondition searchCondition;

  /**
   * 검색 키워드
   */
  private String searchKeyword;

  @Builder
  public BbsListParam(Integer page, Integer pageScale, DelYn delYn, String searchStartDt,
      String searchEndDt, SearchCondition searchCondition, String searchKeyword) {

    this.page = (page == null) ? PAGE : page;
    this.pageScale = (pageScale == null) ? DEFAULT_PAGE_SCALE : pageScale;
    this.delYn = (delYn == null) ? DelYn.N : delYn;
    this.searchStartDt = searchStartDt;
    this.searchEndDt = searchEndDt;
    this.searchCondition = searchCondition;
    this.searchKeyword = searchKeyword;
  }
}
