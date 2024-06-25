package com.kosaf.core.api.code.domain;

import java.time.LocalDateTime;
import com.kosaf.core.api.code.value.CodeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeDetail {
  private String cd;                 // 상세코드 pk
  private String groupCd;            // 그룹코드 fk
  private String cdNm;               // 상세코드명
  private String expln;              // 설명
  private String ordr;               // 코드 순서
  private CodeStatus useYn;          // 사용여부
  private LocalDateTime regDt;       // 등록일시
  private Integer rgtrSysId;         // 등록자 id
  private LocalDateTime mdfcnDt;     // 수정일시
  private Integer mdfrSysId;         // 수정자 id

  @Builder
  public CodeDetail(String cd, String groupCd, String cdNm, String expln, String ordr,
      CodeStatus useYn, LocalDateTime regDt, Integer rgtrSysId, LocalDateTime mdfcnDt,
      Integer mdfrSysId) {
    this.cd = cd;
    this.groupCd = groupCd;
    this.cdNm = cdNm;
    this.expln = expln;
    this.ordr = ordr;
    this.useYn = useYn;
    this.regDt = regDt;
    this.rgtrSysId = rgtrSysId;
    this.mdfcnDt = mdfcnDt;
    this.mdfrSysId = mdfrSysId;
  }

  public void create() {
    this.useYn = CodeStatus.Y;
    this.regDt = LocalDateTime.now();
  }

  public void update() {
    this.mdfcnDt = LocalDateTime.now();
  }

  public void delete() {
    this.useYn = CodeStatus.N;
    this.mdfcnDt = LocalDateTime.now();
  }

}
