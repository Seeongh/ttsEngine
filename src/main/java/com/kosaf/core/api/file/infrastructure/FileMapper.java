package com.kosaf.core.api.file.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.file.application.dto.FileDTO;
import com.kosaf.core.api.file.application.dto.FileDetailDTO;
import com.kosaf.core.api.file.application.dto.FileListParam;
import com.kosaf.core.api.file.application.dto.FileUpdateDTO;
import com.kosaf.core.api.file.domain.File;
import com.kosaf.core.api.file.domain.FileDtl;

@Mapper
public interface FileMapper {

  public Integer countAll(FileListParam param);

  public List<FileDTO> findAll(FileListParam param);

  public FileDetailDTO findById(Long fileDtlSeq);

  public int findMaxFileSn(FileListParam param);

  public Long create(File file);

  public Long createFileDtlMulti(List<FileDtl> list);

  public Long deleteFileDtlMulti(FileUpdateDTO file);
}
