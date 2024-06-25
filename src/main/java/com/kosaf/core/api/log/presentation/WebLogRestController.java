package com.kosaf.core.api.log.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kosaf.core.api.log.application.WebLogService;
import com.kosaf.core.api.log.application.dto.weblog.WebLogDTO;
import com.kosaf.core.api.log.application.dto.weblog.WebLogDetailDTO;
import com.kosaf.core.api.log.application.dto.weblog.WebLogListParam;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WebLogRestController {

  private final WebLogService webLogService;

  @GetMapping("/weblog")
  public ApiResponse<PageResult<WebLogDTO>> findAll(WebLogListParam listParam) {

    return webLogService.findAll(listParam);
  }

  @GetMapping("/weblog/{logSeq}")
  public ApiResponse<WebLogDetailDTO> findBylogSeq(@PathVariable Long logSeq) {

    return webLogService.findByLogSeq(logSeq);
  }

}
