package com.kosaf.core.api.log.application;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.kosaf.core.api.log.application.dto.weblog.WebLogCreateDTO;
import com.kosaf.core.api.log.application.dto.weblog.WebLogDTO;
import com.kosaf.core.api.log.application.dto.weblog.WebLogDetailDTO;
import com.kosaf.core.api.log.application.dto.weblog.WebLogListParam;
import com.kosaf.core.api.log.domain.WebLog;
import com.kosaf.core.api.log.infrastructure.WebLogMapper;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebLogService {

  private final static Logger LOGGER = LoggerFactory.getLogger(WebLogService.class);

  private final WebLogMapper webLogMapper;

  /**
   * 웹 로그 목록
   *
   * @param WebLogListParam
   * @return ApiResponse
   */
  public ApiResponse<PageResult<WebLogDTO>> findAll(WebLogListParam listParam) {

    try {
      PageResult<WebLogDTO> list = PageResult
          .<WebLogDTO>builder()
          .page(listParam.getPage())
          .pageScale(listParam.getPageScale())
          .totalCount(webLogMapper.countAll(listParam))
          .items( Optional
                  .ofNullable(webLogMapper.findAll(listParam))
                  .orElseGet(Collections::emptyList)
                  .stream()
                  .collect(Collectors.toList()))
          .build();

      return list.getItems().isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, list)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 웹 로그 by logSeq
   *
   * @param Long logSeq
   * @return ApiResponse
   */
  public ApiResponse<WebLogDetailDTO> findByLogSeq(Long logSeq) {

    if (logSeq == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      Optional<WebLogDetailDTO> webLog = Optional.ofNullable(webLogMapper.findByLogSeq(logSeq));

      return webLog
          .map(webLogInfo -> ApiResponse.of(HttpStatus.OK, webLogInfo))
          .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 웹 로그 생성
   *
   * @param WebLogCreateDTO
   * @return ApiResponse
   */
  public ApiResponse<Long> create(HttpServletRequest request, WebLogCreateDTO createDTO) {

    if (createDTO == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {

      WebLog webLog = createDTO.toEntity();
      Long result = webLogMapper.create(webLog);

      return result > 0 ? ApiResponse.of(HttpStatus.CREATED, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);

    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error(e.toString());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

}
