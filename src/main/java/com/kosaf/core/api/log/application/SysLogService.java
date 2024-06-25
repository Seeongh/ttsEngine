package com.kosaf.core.api.log.application;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.kosaf.core.api.log.application.dto.syslog.SysLogCreateDTO;
import com.kosaf.core.api.log.application.dto.syslog.SysLogDTO;
import com.kosaf.core.api.log.application.dto.syslog.SysLogDetailDTO;
import com.kosaf.core.api.log.application.dto.syslog.SysLogListParam;
import com.kosaf.core.api.log.domain.SysLog;
import com.kosaf.core.api.log.infrastructure.SysLogMapper;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysLogService {

  private static final Logger LOGGER = LoggerFactory.getLogger(SysLogService.class);

  private final SysLogMapper sysLogMapper;

  /**
   * 시스템 로그 목록
   *
   * @param SysLogListParam
   * @return ApiResponse
   */
  public ApiResponse<PageResult<SysLogDTO>> findAll(SysLogListParam listParam) {

    try {
      PageResult<SysLogDTO> list = PageResult
          .<SysLogDTO>builder()
          .page(listParam.getPage())
          .pageScale(listParam.getPageScale())
          .totalCount(sysLogMapper.countAll(listParam))
          .items( Optional
                  .ofNullable(sysLogMapper.findAll(listParam))
                  .orElseGet(Collections::emptyList)
                  .stream()
                  .collect(Collectors.toList()) )
          .build();

      return list.getItems().isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, list)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 시스템 로그 by logSeq
   *
   * @param Long logSeq
   * @return ApiResponse
   */
  public ApiResponse<SysLogDetailDTO> findBylogSeq(Long logSeq) {
    if( logSeq == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      Optional<SysLogDetailDTO> sysLogInfo = Optional.ofNullable(sysLogMapper.findByLogSeq(logSeq));

      return sysLogInfo
          .map(logInfo -> ApiResponse.of(HttpStatus.OK, logInfo))
          .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  /**
   * 시스템 로그 생성
   *
   * @param SysLogCreateDTO
   * @return ApiResponse
   */
  public ApiResponse<Long> create(SysLogCreateDTO createDTO) {
    if( createDTO == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {

      SysLog sysLog = createDTO.toEntity();
      Long result = sysLogMapper.create(sysLog);

      return result > 0 ? ApiResponse.of(HttpStatus.CREATED, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }
}
