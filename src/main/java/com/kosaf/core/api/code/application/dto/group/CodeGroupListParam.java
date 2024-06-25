package com.kosaf.core.api.code.application.dto.group;

import com.kosaf.core.api.code.value.CodeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CodeGroupListParam {
  private final static int PAGE = 1;
  private final static int DEFAULT_PAGE_SCALE = 10;

  private Integer page = PAGE;
  private Integer pageScale = DEFAULT_PAGE_SCALE;

  private String groupCd;
  private String cdNm;

  private CodeStatus useYn;

  private String searchCon;
  private String searchText;

  @Builder
  public CodeGroupListParam(Integer page, Integer pageScale, String groupCd, String cdNm,
      CodeStatus useYn, String searchCon, String searchText) {
    this.page = page;
    this.pageScale = pageScale;
    this.groupCd = groupCd;
    this.cdNm = cdNm;
    this.useYn = useYn;
    this.searchCon = searchCon;
    this.searchText = searchText;
  }

}
