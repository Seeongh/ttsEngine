package com.kosaf.core.api.menu.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Menu {
  private Long menuNo;

  private String menuNm;
  private String url;
  private String expln;
  private Long parentMenuNo;
  private int ordr;
  private int menuLv;

  private LocalDateTime regDt;
  private Integer rgtrSysId;

  private LocalDateTime mdfcnDt;
  private Integer mdfrSysId;

  @Builder
  public Menu(Long menuNo, String menuNm, String url, String expln, Long parentMenuNo, int ordr,
      int menuLv, LocalDateTime regDt, Integer rgtrSysId, LocalDateTime mdfcnDt,
      Integer mdfrSysId) {
    this.menuNo = menuNo;
    this.menuNm = menuNm;
    this.url = url;
    this.expln = expln;
    this.parentMenuNo = parentMenuNo;
    this.ordr = ordr;
    this.menuLv = menuLv;
    this.regDt = regDt;
    this.rgtrSysId = rgtrSysId;
    this.mdfcnDt = mdfcnDt;
    this.mdfrSysId = mdfrSysId;
  }

  public void create(Integer sysId) {
    this.rgtrSysId = sysId;
    this.regDt = LocalDateTime.now();
    this.mdfrSysId = sysId;
    this.mdfcnDt = LocalDateTime.now();
  }

  public void update(Integer sysId) {
    this.mdfrSysId = sysId;
    this.mdfcnDt = LocalDateTime.now();
  }

}
