package com.kosaf.core.api.log.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogDTO;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogDetailDTO;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogListParam;
import com.kosaf.core.api.log.domain.LoginLog;

@Mapper
public interface LoginLogMapper {

  public Integer countAll(LoginLogListParam listParam);

  public List<LoginLogDTO> findAll(LoginLogListParam listParam);

  public LoginLogDetailDTO findByLogSeq(Long logSeq);

  public Long create(LoginLog loginLog);

}
