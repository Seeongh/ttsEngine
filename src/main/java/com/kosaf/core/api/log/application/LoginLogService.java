package com.kosaf.core.api.log.application;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogCreateDTO;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogDTO;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogDetailDTO;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogListParam;
import com.kosaf.core.api.log.domain.LoginLog;
import com.kosaf.core.api.log.infrastructure.LoginLogMapper;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.ClientInfoUtil;
import com.kosaf.core.common.PageResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginLogService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogService.class);

  private final LoginLogMapper loginLogMapper;

  /**
   * 로그인 로그 목록
   * @param LoginLogListParam
   * @return ApiResponse
   */
  public ApiResponse<PageResult<LoginLogDTO>> findAll(LoginLogListParam listParam) {

    try {
      PageResult<LoginLogDTO> list = PageResult
          .<LoginLogDTO>builder()
          .page(listParam.getPage())
          .pageScale(listParam.getPageScale())
          .totalCount(loginLogMapper.countAll(listParam))
          .items( Optional
                  .ofNullable(loginLogMapper.findAll(listParam))
                  .orElseGet(Collections::emptyList)
                  .stream()
                  .collect(Collectors.toList()) )
          .build();

      return list.getItems().isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, list)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 로그인 로그 by logSeq
   * @param Long logSeq
   * @return ApiResponse
   */
  public ApiResponse<LoginLogDetailDTO> findBylogSeq(Long logSeq) {
    if( logSeq == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      Optional<LoginLogDetailDTO> detailDTO = Optional
          .ofNullable(loginLogMapper.findByLogSeq(logSeq));

      return detailDTO
          .map(logInfo -> ApiResponse.of(HttpStatus.OK, logInfo))
          .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  /**
   * 로그인 로그 생성
   * @param LoginLogCreateDTO
   * @return ApiResponse
   */
  public ApiResponse<Long> create(HttpServletRequest request, LoginLogCreateDTO createDTO) {
    if (createDTO == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      LoginLogCreateDTO dto = createDTO
          .toBuilder()
          .conectIp(ClientInfoUtil.getIp(request))
          .conectOs(ClientInfoUtil.getOs(request))
          .conectBrowser(ClientInfoUtil.getBrowser(request))
          .build();

      LoginLog loginLog = dto.toEntity();
      Long result = loginLogMapper.create(loginLog);

      return result > 0 ? ApiResponse.of(HttpStatus.CREATED, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

}
