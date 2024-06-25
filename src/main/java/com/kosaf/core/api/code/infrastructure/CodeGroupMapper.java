package com.kosaf.core.api.code.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.code.application.dto.group.CodeGroupDetailDTO;
import com.kosaf.core.api.code.application.dto.group.CodeGroupListDTO;
import com.kosaf.core.api.code.application.dto.group.CodeGroupListParam;
import com.kosaf.core.api.code.domain.CodeGroup;

@Mapper
public interface CodeGroupMapper {

  public List<CodeGroupListDTO> findAll(CodeGroupListParam param);

  public Integer countAll(CodeGroupListParam param);

  public CodeGroupDetailDTO findByGroupCd(String groupCd);

  public Long create(CodeGroup code);

  public Long update(CodeGroup code);

  public Long delete(CodeGroup code);

}
