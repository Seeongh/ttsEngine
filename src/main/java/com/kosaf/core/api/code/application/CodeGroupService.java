package com.kosaf.core.api.code.application;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.kosaf.core.api.code.application.dto.group.CodeGroupCreateDTO;
import com.kosaf.core.api.code.application.dto.group.CodeGroupDetailDTO;
import com.kosaf.core.api.code.application.dto.group.CodeGroupListDTO;
import com.kosaf.core.api.code.application.dto.group.CodeGroupListParam;
import com.kosaf.core.api.code.application.dto.group.CodeGroupUpdateDTO;
import com.kosaf.core.api.code.domain.CodeGroup;
import com.kosaf.core.api.code.infrastructure.CodeGroupMapper;
import com.kosaf.core.api.code.value.CodeStatus;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeGroupService {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private final CodeGroupMapper codeMapper;

  /**
   * 그룹코드 목록
   * @param CodeGroupListParam
   * @return ApiResponse
   */
  public ApiResponse<PageResult<CodeGroupListDTO>> findAll(CodeGroupListParam param) {

    try {
      PageResult<CodeGroupListDTO> list = PageResult
          .<CodeGroupListDTO>builder()
          .page(param.getPage())
          .pageScale(param.getPageScale())
          .totalCount(codeMapper.countAll(param))
          .items( Optional
                .ofNullable(codeMapper.findAll(param))
                .orElseGet(Collections::emptyList)
                .stream()
                .collect(Collectors.toList()) )
          .build();

      return list.getItems().isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 그룹코드 by groupCd
   * @param String groupCd
   * @return ApiResponse
   */
  public ApiResponse<CodeGroupDetailDTO> findByGroupCd(String groupCd) {
    if( groupCd == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      ApiResponse<CodeGroupDetailDTO> codeResponse = codeInfo(groupCd);

      return Optional
          .ofNullable(codeResponse.getData())
          .map(codeInfo -> ApiResponse.of(HttpStatus.OK, codeInfo))
          .orElse(ApiResponse.of(HttpStatus.valueOf(codeResponse.getStatus()), null));

    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 그룹코드 생성
   * @param CodeGroupCreateDTO
   * @return ApiResponse
   */
  @Transactional
  public ApiResponse<Long> create(CodeGroupCreateDTO createDto) {
    if ( createDto == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    // 사용자 인증 체크
    ApiResponse<CustomUserDetails> auth = authInfo();
    if ( auth.getStatus() != HttpStatus.OK.value() ) {
      return ApiResponse.of(HttpStatus.valueOf(auth.getStatus()), null);
    }

    // groupCd 존재 여부
    ApiResponse<CodeGroupDetailDTO> codeResponse = codeInfo(createDto.getGroupCd());
    if ( codeResponse.getStatus() == HttpStatus.OK.value() ) {
      return ApiResponse.of(HttpStatus.CONFLICT, null);
    }

    CodeGroupCreateDTO setCreateDTO = createDto
        .toBuilder()
        .rgtrSysId(auth.getData().getSysId())
        .build();

    try {
      CodeGroup code = setCreateDTO.toEntity();
      code.create();
      Long result = codeMapper.create(code);

      return result > 0 ? ApiResponse.of(HttpStatus.CREATED, result)
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 그룹코드 수정
   * @param CodeGroupCreateDTO
   * @return ApiResponse
   */
  @Transactional
  public ApiResponse<CodeGroupUpdateDTO> update(CodeGroupUpdateDTO updateDTO) {
    if ( updateDTO == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    // 사용자 인증 체크
    ApiResponse<CustomUserDetails> auth = authInfo();
    if ( auth.getStatus() != HttpStatus.OK.value() ) {
      return ApiResponse.of(HttpStatus.valueOf(auth.getStatus()), null);
    }

    CodeGroupUpdateDTO setUpdateDTO = updateDTO
        .toBuilder()
        .mdfrSysId(auth.getData().getSysId())
        .build();

    try {

      CodeGroup code = setUpdateDTO.toEntity();
      code.update();
      Long result = codeMapper.update(code);

      return result > 0 ? ApiResponse.of(HttpStatus.OK, updateDTO)
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 그룹코드 삭제
   * @param CodeGroupCreateDTO
   * @return ApiResponse
   */
  @Transactional
  public ApiResponse<Long> delete(String groupCd) {

    // 사용자 인증 체크
    ApiResponse<CustomUserDetails> auth = authInfo();
    if ( auth.getStatus() != HttpStatus.OK.value() ) {
      return ApiResponse.of(HttpStatus.valueOf(auth.getStatus()), null);
    }

    // 이미 삭제된 데이터일 경우
    ApiResponse<CodeGroupDetailDTO> codeResponse = codeInfo(groupCd);
    CodeGroupDetailDTO codeInfo = codeResponse.getData();

    if ( CodeStatus.N.equals(codeInfo.getUseYn()) ) {
      return ApiResponse.of(HttpStatus.NOT_FOUND, null);
    }

    CodeGroupUpdateDTO updateDTO = CodeGroupUpdateDTO
        .builder()
        .mdfrSysId(auth.getData().getSysId())
        .build();

    try {
      CodeGroup code = updateDTO.toEntity();
      code.delete();
      Long result = codeMapper.delete(code);

      return result > 0 ? ApiResponse.of(HttpStatus.NO_CONTENT, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 그룹코드 정보
   * @param String groupCd
   * @return ApiResponse
   */
  public ApiResponse<CodeGroupDetailDTO> codeInfo (String groupCd) {

    try {
      // ofNullable -> 값이 null일수도 아닐수도 있는 경우 orElse 또는 orElseGet 으로 안전하게 가져옴
      Optional<CodeGroupDetailDTO> codeInfo = Optional.ofNullable(codeMapper.findByGroupCd(groupCd));

      return codeInfo
          .map(code -> ApiResponse.of(HttpStatus.OK, code))
          .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 인증 유저 객체
   * @return ApiResponse
   */
  public ApiResponse<CustomUserDetails> authInfo() {
    Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

    if ( loggedInUser.getDetails() instanceof CustomUserDetails ) {
      LOGGER.info("authrized user : {}", loggedInUser.getDetails().getClass() );
      return ApiResponse.of(HttpStatus.OK, (CustomUserDetails) loggedInUser.getDetails());

    } else {
      LOGGER.error("unauthrized user : {}", loggedInUser.getDetails().getClass() );
      return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);
    }
  }
}
