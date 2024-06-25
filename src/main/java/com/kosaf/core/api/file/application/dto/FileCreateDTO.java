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
public class FileCreateDTO {

  // File, FileDtl Create를 위한 DTO

  private Long fileSeq;

  private int fileSn;
  private String fileStrePath;
  private String streFileNm;
  private String orignlFileNm;
  private String fileExtsn;
  private Long fileSize;

  @Enumerated(EnumType.STRING)
  private DelYn delYn;

  private LocalDateTime regDt;
  private Long rgtrSysId;
  private LocalDateTime mdfcnDt;
  private Long mdfrSysId;

  private String dirNm;

  public FileDtl toEntity() {
    return FileDtl
        .builder()
        .fileSeq(fileSeq)
        .fileSn(fileSn)
        .fileStrePath(fileStrePath)
        .streFileNm(streFileNm)
        .orignlFileNm(orignlFileNm)
        .fileExtsn(fileExtsn)
        .fileSize(fileSize)
        .rgtrSysId(rgtrSysId)
        .mdfrSysId(mdfrSysId)
        .build();
  }

}
