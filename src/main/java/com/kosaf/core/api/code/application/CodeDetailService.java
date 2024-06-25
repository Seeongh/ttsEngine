package com.kosaf.core.api.code.application;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailCreateDTO;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailDTO;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailListDTO;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailListParam;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailUpdateDTO;
import com.kosaf.core.api.code.domain.CodeDetail;
import com.kosaf.core.api.code.infrastructure.CodeDetailMapper;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeDetailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CodeDetailService.class);

  private final CodeDetailMapper codeDetailMapper;

  /**
   * 상세코드 목록
   * @param CodeDetailListParam
   * @return ApiResponse
   */
  public ApiResponse<PageResult<CodeDetailListDTO>> findAll(CodeDetailListParam param) {

    try {
      PageResult<CodeDetailListDTO> list = PageResult
          .<CodeDetailListDTO>builder()
          .page(param.getPage())
          .pageScale(param.getPageScale())
          .totalCount(codeDetailMapper.countAll(param))
          .items( Optional
                  .ofNullable(codeDetailMapper.findAll(param))
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
   * 상세코드 목록 by groupCd
   * @param String groupCd
   * @return ApiResponse
   */
  public ApiResponse<List<CodeDetailDTO>> findAllByGroupCd(String groupCd) {
    try {

      CodeDetailDTO codeDetailDTO = CodeDetailDTO
          .builder()
          .groupCd(groupCd)
          .build();

      List<CodeDetailDTO> list = codeDetailMapper.findAllByGroupCd(codeDetailDTO);

      return list.isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }


  /**
   * 상세코드 by groupCd, cd
   * @param String groupCd, String cd
   * @return ApiResponse
   */
  public ApiResponse<CodeDetailDTO> findByGroupCdAndCd(String groupCd, String cd) {
    if ( cd == null || groupCd == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      ApiResponse<CodeDetailDTO> detailResponse = codeDetailInfo(groupCd, cd);

      return Optional
         .ofNullable(detailResponse.getData())
         .map(detailInfo -> ApiResponse.of(HttpStatus.OK, detailInfo))
         .orElse(ApiResponse.of(HttpStatus.valueOf(detailResponse.getStatus()), null));

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 상세코드 생성
   * @param CodeDetailCreateDTO
   * @return ApiResponse
   */
  @Transactional
  public ApiResponse<Long> create(CodeDetailCreateDTO createDTO){
    if ( createDTO == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    // 사용자 인증 체크
    ApiResponse<CustomUserDetails> auth = authInfo();
    if ( auth.getStatus() != HttpStatus.OK.value() ) {
      return ApiResponse.of(HttpStatus.valueOf(auth.getStatus()), null);
    }

    // groupCd, cd 이미 존재할 경우
    ApiResponse<CodeDetailDTO> detailResponse = codeDetailInfo(createDTO.getGroupCd(), createDTO.getCd());
    if ( detailResponse.getStatus() == HttpStatus.OK.value() ) {
      LOGGER.error("group : {}, cd: {} - already exists", createDTO.getGroupCd(), createDTO.getCd());
      return ApiResponse.of(HttpStatus.CONFLICT, null);
    }

    CodeDetailCreateDTO setCreatDTO = createDTO
        .toBuilder()
        .rgtrSysId(auth.getData().getSysId())
        .build();

    try {
      CodeDetail codeDetail = setCreatDTO.toEntity();
      codeDetail.create();
      Long result = codeDetailMapper.create(codeDetail);

      return result > 0 ? ApiResponse.of(HttpStatus.CREATED, result)
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  /**
   * 상세코드 수정
   * @param CodeDetailUpdateDTO
   * @return CodeDetail
   */
  @Transactional
  public ApiResponse<Long> update(CodeDetailUpdateDTO updateDTO) {
    if ( updateDTO == null ) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    // 사용자 인증 체크
    ApiResponse<CustomUserDetails> auth = authInfo();
    if ( auth.getStatus() != HttpStatus.OK.value() ) {
      return ApiResponse.of(HttpStatus.valueOf(auth.getStatus()), null);
    }

    CodeDetailUpdateDTO setUpdateDTO = updateDTO
        .toBuilder()
        .mdfrSysId(auth.getData().getSysId())
        .build();

    try {

      CodeDetail codeDetail = setUpdateDTO.toEntity();
      codeDetail.update();
      Long result = codeDetailMapper.update(codeDetail);

      return result > 0 ? ApiResponse.of(HttpStatus.OK, null)
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 상세코드 정보
   * @param String cd
   * @return ApiResponse
   */
  private ApiResponse<CodeDetailDTO> codeDetailInfo(String groupCd, String cd) {

    try {

      CodeDetailDTO codeDetailDTO = CodeDetailDTO
          .builder()
          .groupCd(groupCd)
          .cd(cd)
          .build();

      Optional<CodeDetailDTO> detailInfo = Optional.ofNullable(codeDetailMapper.findByGroupCdAndCd(codeDetailDTO));

      return detailInfo
          .map(detail -> ApiResponse.of(HttpStatus.OK, detail))
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
