package com.kosaf.core.api.code.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailDTO;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailListDTO;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailListParam;
import com.kosaf.core.api.code.domain.CodeDetail;

@Mapper
public interface CodeDetailMapper {

  public List<CodeDetailListDTO> findAll(CodeDetailListParam param);

  public Integer countAll(CodeDetailListParam param);

  public List<CodeDetailDTO> findAllByGroupCd(CodeDetailDTO detailDTO);

  public CodeDetailDTO findByGroupCdAndCd(CodeDetailDTO detailDTO);

  public Long create(CodeDetail codeDetail);

  public Long update(CodeDetail codeDetail);

  public Long delete(CodeDetail codeDetail);

}
