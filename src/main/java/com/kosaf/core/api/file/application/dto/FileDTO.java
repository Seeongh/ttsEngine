package com.kosaf.core.api.file.application.dto;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.file.domain.FileDtl;
import com.kosaf.core.api.file.value.DelYn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FileDTO {

  // File, FileDtl 목록 조회를 위한 DTO

  private Long fileSeq;
  private Long fileDtlSeq;

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

  private String rgtrSysNm;

  public static FileDTO of(FileDtl fileDtl) {
    return FileDTO
        .builder()
        .fileSeq(fileDtl.getFileSeq())
        .fileDtlSeq(fileDtl.getFileDtlSeq())
        .fileSn(fileDtl.getFileSn())
        .fileStrePath(fileDtl.getFileStrePath())
        .streFileNm(fileDtl.getStreFileNm())
        .orignlFileNm(fileDtl.getOrignlFileNm())
        .fileExtsn(fileDtl.getFileExtsn())
        .fileSize(fileDtl.getFileSize())
        .delYn(fileDtl.getDelYn())
        .regDt(fileDtl.getRegDt())
        .rgtrSysId(fileDtl.getRgtrSysId())
        .build();
  }
}
