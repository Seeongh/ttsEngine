package com.kosaf.core.api.code.application.dto.detail;

import java.time.LocalDateTime;
import java.util.Map;
import com.kosaf.core.api.code.domain.CodeDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CodeDetailListDTO {

  private String listNo;
  private String groupCd;
  private String groupCdNm;
  private String cd;
  private String cdNm;
  private String expln;
  private String ordr;
  private String useYn;
  private LocalDateTime regDt;

  private Map<String, String> codeStatusMap;

  // entity to dto
  public static CodeDetailListDTO of(CodeDetail codeDetail) {
    return CodeDetailListDTO
        .builder()
        .cd(codeDetail.getCd())
        .groupCd(codeDetail.getGroupCd())
        .cdNm(codeDetail.getCdNm())
        .expln(codeDetail.getExpln())
        .ordr(codeDetail.getOrdr())
        .useYn(codeDetail.getUseYn().getCode())
        .regDt(codeDetail.getRegDt())
        .build();
  }



}
