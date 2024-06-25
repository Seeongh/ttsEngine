package com.kosaf.core.api.menu.application.dto;

import java.time.LocalDateTime;
import com.kosaf.core.api.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MenuListDTO {
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

  public static MenuListDTO of(Menu menu) {
    return MenuListDTO
        .builder()
        .menuNo(menu.getMenuNo())
        .menuNm(menu.getMenuNm())
        .url(menu.getUrl())
        .expln(menu.getExpln())
        .parentMenuNo(menu.getParentMenuNo())
        .ordr(menu.getOrdr())
        .menuLv(menu.getMenuLv())
        .regDt(menu.getRegDt())
        .rgtrSysId(menu.getRgtrSysId())
        .mdfcnDt(menu.getMdfcnDt())
        .mdfrSysId(menu.getMdfrSysId())
        .build();
  }
}
