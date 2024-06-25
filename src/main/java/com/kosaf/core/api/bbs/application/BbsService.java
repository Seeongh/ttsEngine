package com.kosaf.core.api.bbs.application;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.kosaf.core.api.bbs.application.dto.BbsCreateDTO;
import com.kosaf.core.api.bbs.application.dto.BbsDTO;
import com.kosaf.core.api.bbs.application.dto.BbsDetailDTO;
import com.kosaf.core.api.bbs.application.dto.BbsListParam;
import com.kosaf.core.api.bbs.application.dto.BbsUpdateDTO;
import com.kosaf.core.api.bbs.domain.Bbs;
import com.kosaf.core.api.bbs.infrastructure.BbsMapper;
import com.kosaf.core.api.bbs.value.DelYn;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.ExcelUtil;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BbsService {

  private final BbsMapper bbsMapper;

  /**
   * BBS 리스트 조회
   * 
   * @param param
   * @return ApiResponse<PageResult<BbsDTO>>
   */
  public ApiResponse<PageResult<BbsDTO>> findAll(BbsListParam param) {

    try {
      PageResult<BbsDTO> list = PageResult
          .<BbsDTO>builder()
          .page(param.getPage())
          .pageScale(param.getPageScale())
          .totalCount(bbsMapper.countAll(param))
          .items(Optional
              .ofNullable(bbsMapper.findAll(param))
              .orElseGet(Collections::emptyList)
              .stream()
              .collect(Collectors.toList()))
          .build();

      return list.getItems().isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * BBS 리스트 엑셀 다운로드
   * 
   * @param param
   * @param req
   * @param res
   */
  public void excelDown(BbsListParam param, HttpServletRequest req, HttpServletResponse res) {

    List<BbsDTO> list = bbsMapper.findAll(param);

    String headNms = "No,제목,등록일시,등록자,첨부파일,조회수";
    String fields = "rownum,title,regDt,rgtrSysNm,fileYn,inqCnt";

    ExcelUtil excelUtil = new ExcelUtil("게시글_목록", headNms, fields, req, res);
    excelUtil.setDataToExcelList(list, "목록");
    excelUtil.writeExcel();
  }

  /**
   * BBS 상세 조회
   * 
   * @param param
   * @return ApiResponse<BbsDetailDTO>
   */
  public ApiResponse<BbsDetailDTO> findById(Long bbsSeq) {

    if (bbsSeq == null)
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);

    try {

      ApiResponse<BbsDetailDTO> bbsResponse = bbsInfo(bbsSeq);

      if (bbsResponse.getData() == null || DelYn.Y.equals(bbsResponse.getData().getDelYn()))
        return ApiResponse.of(HttpStatus.NOT_FOUND, null);

      return Optional
          .ofNullable(bbsResponse.getData())
          .map(bbs -> ApiResponse.of(HttpStatus.OK, bbs))
          .orElse(ApiResponse.of(HttpStatus.valueOf(bbsResponse.getStatus()), null));
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * BBS 상세 조회
   * 
   * @param bbsSeq
   * @return ApiResponse<Bbs>
   */
  public ApiResponse<BbsDetailDTO> bbsInfo(Long bbsSeq) {

    try {
      Optional<BbsDetailDTO> bbsInfo = Optional.ofNullable(bbsMapper.findById(bbsSeq));

      return bbsInfo
          .map(bbsDetailDTO -> ApiResponse.of(HttpStatus.OK, bbsDetailDTO))
          .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * BBS 등록
   * 
   * @param createDTO
   * @return ApiResponse<Long>
   */
  @Transactional
  public ApiResponse<Long> create(BbsCreateDTO createDTO) {

    if (createDTO == null || createDTO.getTitle() == null || createDTO.getContents() == null)
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);

    try {

      // 로그인 확인
      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

      if (loginUser == null || loginUser.getSysId() == null)
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);

      Bbs bbs = createDTO.toEntity();
      bbs.create(Long.valueOf(loginUser.getSysId()));

      // BBS 등록
      Long result = bbsMapper.create(bbs);

      return result > 0 ? ApiResponse.of(HttpStatus.CREATED, bbs.getBbsSeq())
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * BBS 수정
   * 
   * @param createDTO
   * @return ApiResponse<Long>
   */
  @Transactional
  public ApiResponse<Long> update(BbsUpdateDTO updateDTO) {

    if (updateDTO == null || updateDTO.getBbsSeq() == null || updateDTO.getTitle() == null
        || updateDTO.getContents() == null)
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);

    // 수정 데이터 조회
    ApiResponse<BbsDetailDTO> bbsResponse = bbsInfo(updateDTO.getBbsSeq());

    if (bbsResponse.getStatus() != HttpStatus.OK.value())
      return ApiResponse.of(HttpStatus.valueOf(bbsResponse.getStatus()), null);

    try {

      // 로그인 확인
      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

      if (loginUser == null || loginUser.getSysId() == null)
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);

      // 등록자 확인
      if (bbsResponse.getData().getRgtrSysId() != Long.valueOf(loginUser.getSysId()))
        return ApiResponse.of(HttpStatus.BAD_REQUEST, null);

      Bbs bbs = updateDTO.toEntity();
      bbs.update(Long.valueOf(loginUser.getSysId()));

      // BBS 수정
      Long result = bbsMapper.update(bbs);

      return result > 0 ? ApiResponse.of(HttpStatus.OK, bbs.getBbsSeq())
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);

    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * 조회수 증가
   * 
   * @param bbsSeq
   * @return ApiResponse<Void>
   */
  @Transactional
  public ApiResponse<Void> updateInqCnt(Long bbsSeq) {
    if (bbsSeq == null)
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);

    try {
      // 로그인 확인
      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

      if (loginUser == null || loginUser.getSysId() == null)
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);

      // 조회수 증가(등록자와 같이 않을 경우만 증가)
      Bbs updateBbs = BbsUpdateDTO.builder().bbsSeq(bbsSeq).delYn(DelYn.N).build().toEntity();
      updateBbs.update(Long.valueOf(loginUser.getSysId()));
      bbsMapper.updateInqCnt(updateBbs);

      return ApiResponse.of(HttpStatus.OK, null);
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  /**
   * BBS 삭제
   * 
   * @param createDTO
   * @return ApiResponse<Void>
   */
  @Transactional
  public ApiResponse<Void> delete(Long bbsSeq) {

    if (bbsSeq == null)
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);

    // 삭제 데이터 조회
    ApiResponse<BbsDetailDTO> bbsResponse = bbsInfo(bbsSeq);

    if (bbsResponse.getStatus() != HttpStatus.OK.value())
      return ApiResponse.of(HttpStatus.valueOf(bbsResponse.getStatus()), null);

    try {
      Bbs bbs = Bbs.builder().bbsSeq(bbsSeq).delYn(bbsResponse.getData().getDelYn()).build();

      // 삭제 여부 확인
      if (DelYn.Y.equals(bbs.getDelYn()))
        return ApiResponse.of(HttpStatus.NOT_FOUND, null);

      // 로그인 확인
      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

      // 로그인 사용자 확인 및 등록자와 같은지 확인
      if (loginUser == null || loginUser.getSysId() == null)
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);

      // 등록자 확인
      if (bbsResponse.getData().getRgtrSysId() != Long.valueOf(loginUser.getSysId()))
        return ApiResponse.of(HttpStatus.BAD_REQUEST, null);

      bbs.delete(Long.valueOf(loginUser.getSysId()));

      // BBS 삭제
      Long result = bbsMapper.delete(bbs);

      return result > 0 ? ApiResponse.of(HttpStatus.NO_CONTENT, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);

    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }
}
