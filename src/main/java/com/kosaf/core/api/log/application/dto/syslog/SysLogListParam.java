package com.kosaf.core.api.log.application.dto.syslog;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.log.value.ProcessSection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SysLogListParam {

  private Long logSeq;

  private final static int PAGE = 1;
  private final static int DEFAULT_PAGE_SCALE = 10;

  private Integer page = PAGE;
  private Integer pageScale = DEFAULT_PAGE_SCALE;

  private String startOccrrncDe;    // 시작일
  private String endOccrrncDe;      // 종료일

  @Enumerated(EnumType.STRING)
  private ProcessSection processSeCd;

  private String errorYn;

  private String searchCon;
  private String searchText;

  @Builder
  public SysLogListParam(Long logSeq, Integer page, Integer pageScale, String startOccrrncDe,
      String endOccrrncDe, ProcessSection processSeCd, String searchCon, String searchText,
      String errorYn) {
    this.logSeq = logSeq;
    this.page = page;
    this.pageScale = pageScale;
    this.startOccrrncDe = startOccrrncDe;
    this.endOccrrncDe = endOccrrncDe;
    this.processSeCd = processSeCd;
    this.searchCon = searchCon;
    this.searchText = searchText;
    this.errorYn = errorYn;
  }

}
