package com.kosaf.core.api.menu.application.dto;

import java.time.LocalDateTime;
import com.kosaf.core.api.menu.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateDTO {

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

  public Menu toEntity() {

    return Menu
        .builder()
        .menuNo(menuNo)
        .menuNm(menuNm)
        .url(url)
        .expln(expln)
        .parentMenuNo(parentMenuNo)
        .ordr(ordr)
        .menuLv(menuLv)
        .regDt(regDt)
        .rgtrSysId(rgtrSysId)
        .mdfcnDt(mdfcnDt)
        .mdfrSysId(mdfrSysId)
        .build();

  }

}
