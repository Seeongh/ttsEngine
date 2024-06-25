package com.kosaf.core.api.file.application;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.kosaf.core.api.file.application.dto.FileCreateDTO;
import com.kosaf.core.api.file.application.dto.FileDTO;
import com.kosaf.core.api.file.application.dto.FileDetailDTO;
import com.kosaf.core.api.file.application.dto.FileListParam;
import com.kosaf.core.api.file.application.dto.FileUpdateDTO;
import com.kosaf.core.api.file.domain.File;
import com.kosaf.core.api.file.domain.FileDtl;
import com.kosaf.core.api.file.infrastructure.FileMapper;
import com.kosaf.core.api.file.value.DelYn;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.FileUtil;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileService {

  private final Log logger = LogFactory.getLog(getClass());

  private final FileMapper fileMapper;

  private final FileUtil fileUtil;

  @Value("${spring.servlet.multipart.max-request-size}")
  private String MAX_REQUEST_SIZE;

  @Value("${spring.servlet.multipart.max-file-size}")
  private String MAX_FILE_SIZE;

  // 단위 변환을 위한 상수 정의
  private static final long KB = 1024L;
  private static final long MB = 1024L * KB;
  private static final long GB = 1024L * MB;

  /**
   * multipart config 파일 사이즈 취득
   * 
   * @return ApiResponse<Map<String, Object>>
   */
  public ApiResponse<Map<String, Object>> getMultipartConfig() {
    Map<String, Object> map = new HashMap<String, Object>();

    map.put("maxRequestSize", convertToBytes(MAX_REQUEST_SIZE));
    map.put("maxFileSize", convertToBytes(MAX_FILE_SIZE));

    return ApiResponse.of(HttpStatus.OK, map);
  }

  /**
   * byte 변환
   * 
   * @param sizeStr
   * @return long
   */
  public long convertToBytes(String sizeStr) {
    if (sizeStr == null || sizeStr.isEmpty())
      return 0;

    sizeStr = sizeStr.toUpperCase().trim();

    long size = Long.parseLong(sizeStr.replaceAll("[^\\d]", ""));
    String unit = sizeStr.replaceAll("[\\d]", "");

    switch (unit) {
      case "GB":
        return size * GB;
      case "MB":
        return size * MB;
      case "KB":
        return size * KB;
      default:
        return size;
    }
  }

  /**
   * 파일 리스트 조회
   * 
   * @param param
   * @return ApiResponse<List<FileDTO>>
   */
  public ApiResponse<List<FileDTO>> findAll(FileListParam param) {

    if (param == null || param.getFileSeq() == null)
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);

    try {

      List<FileDTO> list = Optional
          .ofNullable(fileMapper.findAll(param))
          .orElseGet(Collections::emptyList)
          .stream()
          .collect(Collectors.toList());

      return list.isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  /**
   * 파일 상세 조회(파일 다운로드)
   * 
   * @param fileDtlSeq
   * @return ResponseEntity<Resource>
   */
  public ResponseEntity<Resource> findById(Long fileDtlSeq) {

    try {

      FileDetailDTO info = Optional.ofNullable(fileMapper.findById(fileDtlSeq)).orElseGet(null);

      if (info != null)
        return fileUtil
            .downloadFile(info.getFileStrePath(), info.getStreFileNm(), info.getOrignlFileNm());

    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.notFound().build();
  }

  /**
   * 파일 등록
   * 
   * @param fileParam
   * @param multiRequest
   * @return ApiResponse<Long>
   */
  @Transactional
  public ApiResponse<Long> create(FileCreateDTO createDTO,
      MultipartHttpServletRequest multiRequest) {

    try {
      final Map<String, MultipartFile> files = multiRequest.getFileMap();

      // 로그인 확인
//      CustomUserDetails loginUser =
//          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//      if (loginUser == null || loginUser.getSysId() == null)
//        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);
//
//      if (files != null && !files.isEmpty()) {
//
//        // 등록자/수정자 설정
//        createDTO.setRgtrSysId(Long.valueOf(loginUser.getSysId()));
//        createDTO.setMdfrSysId(Long.valueOf(loginUser.getSysId()));

      createDTO.setRgtrSysId(1L);
      createDTO.setMdfrSysId(1L);
      if(files != null && !files.isEmpty()){
        File file = File.builder().rgtrSysId(createDTO.getRgtrSysId()).build();
        file.create();

        // 파일 등록
        fileMapper.create(file);
        createDTO.setFileSeq(file.getFileSeq());
        createDTO.setFileSn(1);

        // 파일 업로드
        List<FileDtl> list = fileUtil.uploadFile(createDTO, files);

        // 파일 상세 등록
        if (!list.isEmpty()) {
          Long result = fileMapper.createFileDtlMulti(list);

          return (result > 0) ? ApiResponse.of(HttpStatus.CREATED, createDTO.getFileSeq())
              : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
        }
      }

    } catch (IOException e) {
      logger.error("[fileService.insertFile] IOException: " + e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    } catch (Exception e) {
      logger.error("[fileService.insertFile] Exception : " + e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    return ApiResponse.of(HttpStatus.OK, null);
  }

  /**
   * 첨부파일 수정
   * 
   * @param updateDTO
   * @param multiRequest
   * @return ApiResponse<Long>
   */
  @Transactional
  public ApiResponse<Long> update(FileUpdateDTO updateDTO,
      MultipartHttpServletRequest multiRequest) {

    if (updateDTO == null || updateDTO.getFileSeq() == null)
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);

    try {
      // 로그인 확인
      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

      if (loginUser == null || loginUser.getSysId() == null)
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);

      final Map<String, MultipartFile> files = multiRequest.getFileMap();

      updateDTO.setMdfrSysId(Long.valueOf(loginUser.getSysId()));
      updateDTO.delete();

      // 파일 상세 멀티 삭제
      fileMapper.deleteFileDtlMulti(updateDTO);

      // 파일 순서 조회 param
      FileListParam param =
          FileListParam.builder().delYn(DelYn.N).fileSeq(updateDTO.getFileSeq()).build();

      // 파일 등록
      if (files != null && !files.isEmpty()) {

        FileCreateDTO createDTO = FileCreateDTO
            .builder()
            .fileSeq(updateDTO.getFileSeq())
            .fileSn(fileMapper.findMaxFileSn(param))
            .dirNm(updateDTO.getDirNm())
            .rgtrSysId(Long.valueOf(loginUser.getSysId()))
            .mdfrSysId(Long.valueOf(loginUser.getSysId()))
            .build();

        // 파일 업로드
        List<FileDtl> list = fileUtil.uploadFile(createDTO, files);

        // 파일 상세 등록
        if (!list.isEmpty())
          fileMapper.createFileDtlMulti(list);
        else
          return ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
      }

      // 파일 수 확인
      Integer result = fileMapper.countAll(param);

      return (result > 0) ? ApiResponse.of(HttpStatus.OK, updateDTO.getFileSeq())
          : ApiResponse.of(HttpStatus.NO_CONTENT, null);

    } catch (IOException e) {
      logger.error("[fileService.insertFile] IOException: " + e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    } catch (Exception e) {
      logger.error("[fileService.insertFile] Exception : " + e.getMessage());
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }
}
