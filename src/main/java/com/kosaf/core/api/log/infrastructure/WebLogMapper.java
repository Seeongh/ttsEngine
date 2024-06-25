package com.kosaf.core.api.log.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.log.application.dto.weblog.WebLogDTO;
import com.kosaf.core.api.log.application.dto.weblog.WebLogDetailDTO;
import com.kosaf.core.api.log.application.dto.weblog.WebLogListParam;
import com.kosaf.core.api.log.domain.WebLog;

@Mapper
public interface WebLogMapper {

  public Integer countAll(WebLogListParam listParam);

  public List<WebLogDTO> findAll(WebLogListParam listParam);

  public WebLogDetailDTO findByLogSeq(Long logSeq);

  public Long create(WebLog webLog);
}
