package com.kosaf.core.api.log.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kosaf.core.api.log.application.SysLogService;
import com.kosaf.core.api.log.application.dto.syslog.SysLogDTO;
import com.kosaf.core.api.log.application.dto.syslog.SysLogDetailDTO;
import com.kosaf.core.api.log.application.dto.syslog.SysLogListParam;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SysLogRestController {

  private final SysLogService sysLogService;

  @GetMapping("/syslog")
  public ApiResponse<PageResult<SysLogDTO>> findAll(SysLogListParam param) {
    return sysLogService.findAll(param);
  }

  @GetMapping("/syslog/{logSeq}")
  public ApiResponse<SysLogDetailDTO> findBylogSeq(@PathVariable Long logSeq) {

    return sysLogService.findBylogSeq(logSeq);
  }

}
