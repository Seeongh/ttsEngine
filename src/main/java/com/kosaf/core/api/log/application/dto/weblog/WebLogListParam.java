package com.kosaf.core.api.log.application.dto.weblog;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebLogListParam {
  private Long logSeq;

  private final static int PAGE = 1;
  private final static int DEFAULT_PAGE_SCALE = 10;

  private Integer page = PAGE;
  private Integer pageScale = DEFAULT_PAGE_SCALE;

  private String startOccrrncDe;    // 시작일
  private String endOccrrncDe;      // 종료일

  private String searchCon;
  private String searchText;

  @Builder
  public WebLogListParam(Long logSeq, Integer page, Integer pageScale, String startOccrrncDe,
      String endOccrrncDe, String searchCon, String searchText) {
    this.logSeq = logSeq;
    this.page = page;
    this.pageScale = pageScale;
    this.startOccrrncDe = startOccrrncDe;
    this.endOccrrncDe = endOccrrncDe;
    this.searchCon = searchCon;
    this.searchText = searchText;
  }

}
