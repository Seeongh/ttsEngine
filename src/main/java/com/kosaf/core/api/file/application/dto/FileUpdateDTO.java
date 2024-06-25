package com.kosaf.core.api.file.application.dto;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.file.domain.FileDtl;
import com.kosaf.core.api.file.value.DelYn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUpdateDTO {

  // File, FileDtl Update를 위한 DTO

  private Long fileSeq;
  private Long fileDtlSeq;

  @Enumerated(EnumType.STRING)
  private DelYn delYn;

  private LocalDateTime mdfcnDt;
  private Long mdfrSysId;

  private String dirNm;

  private Long[] fileDtlSeqArr;

  public FileDtl toEntity() {
    return FileDtl.builder().fileSeq(fileSeq).fileDtlSeq(fileDtlSeq).mdfrSysId(mdfrSysId).build();
  }

  public void delete() {
    this.delYn = DelYn.Y;
    this.mdfcnDt = LocalDateTime.now();
  }
}
