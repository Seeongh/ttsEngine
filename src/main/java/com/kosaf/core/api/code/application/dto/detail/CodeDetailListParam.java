package com.kosaf.core.api.code.application.dto.detail;

import com.kosaf.core.api.code.value.CodeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class CodeDetailListParam {
  private final static int PAGE = 1;
  private final static int DEFAULT_PAGE_SCALE = 10;

  private Integer page = PAGE;
  private Integer pageScale = DEFAULT_PAGE_SCALE;

  private String cd;
  private String groupCd;
  private String cdNm;

  private CodeStatus useYn = CodeStatus.Y;

  private String searchCon;
  private String searchText;

  @Builder
  public CodeDetailListParam(Integer page, Integer pageScale, String cd, String groupCd,
      String cdNm, CodeStatus useYn, String searchCon, String searchText) {
    this.page = page;
    this.pageScale = pageScale;
    this.cd = cd;
    this.groupCd = groupCd;
    this.cdNm = cdNm;
    this.useYn = useYn;
    this.searchCon = searchCon;
    this.searchText = searchText;
  }

}
