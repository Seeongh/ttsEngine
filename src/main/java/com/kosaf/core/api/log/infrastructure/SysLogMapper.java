package com.kosaf.core.api.log.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.log.application.dto.syslog.SysLogDTO;
import com.kosaf.core.api.log.application.dto.syslog.SysLogDetailDTO;
import com.kosaf.core.api.log.application.dto.syslog.SysLogListParam;
import com.kosaf.core.api.log.domain.SysLog;

@Mapper
public interface SysLogMapper {

  public Integer countAll(SysLogListParam listParam);

  public List<SysLogDTO> findAll(SysLogListParam listParam);

  public SysLogDetailDTO findByLogSeq(Long logSeq);

  public Long create(SysLog sysLog);

}
