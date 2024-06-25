package com.kosaf.core.api.log.application.dto.loginlog;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.log.value.ConnectionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginLogListParam {

  // 검색 조건

  private final static int PAGE = 1;
  private final static int DEFAULT_PAGE_SCALE = 10;

  private Integer page = PAGE;
  private Integer pageScale = DEFAULT_PAGE_SCALE;

  private String startLogDt;    // 시작일
  private String endLogDt;      // 종료일

  @Enumerated(EnumType.STRING)
  private ConnectionStatus conectSttusCd;

  private String searchCon;
  private String searchText;

  @Builder
  public LoginLogListParam(Integer page, Integer pageScale, String startLogDt, String endLogDt,
      ConnectionStatus conectSttusCd, String searchCon, String searchText) {
    this.page = page;
    this.pageScale = pageScale;
    this.startLogDt = startLogDt;
    this.endLogDt = endLogDt;
    this.conectSttusCd = conectSttusCd;
    this.searchCon = searchCon;
    this.searchText = searchText;
  }

}
