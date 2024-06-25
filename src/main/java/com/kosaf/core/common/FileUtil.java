package com.kosaf.core.common;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.kosaf.core.api.file.application.dto.FileCreateDTO;
import com.kosaf.core.api.file.domain.FileDtl;

@Component
public class FileUtil {

  private final Log logger = LogFactory.getLog(getClass());

  private final static String DIR_NM = "ETC";

  @Value("${spring.servlet.multipart.location}")
  private String UPLOAD_PATH;

  /**
   * 파일 업로드
   * 
   * @param param
   * @param files
   * @return List<FileDtl>
   * @throws Exception
   */
  public List<FileDtl> uploadFile(FileCreateDTO param, Map<String, MultipartFile> files)
      throws Exception {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    Date currentDate = new Date();

    // 파일 경로
    String dirNm = StringUtils.isEmpty(param.getDirNm()) ? DIR_NM : param.getDirNm();
    String subDir = dirNm + File.separator + dateFormat.format(currentDate) + File.separator;
    String basePath = UPLOAD_PATH + subDir;

    // 파일 경로 확인 및 생성
    File fileStrePath = new File(basePath);
    if (!fileStrePath.exists()) {
      if (!fileStrePath.mkdirs())
        throw new Exception("[fileUtil.mkdirs] saveFolder : Creation Fail");
    }

    List<FileDtl> fileList = new ArrayList<>();

    // 파일 순서
    int fileSn = param.getFileSn();
    for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
      MultipartFile file = entry.getValue();

      // 원본 파일명
      String orignlFileNm = file.getOriginalFilename();

      if (StringUtils.isEmpty(orignlFileNm))
        continue;

      // 파일 확장자
      String fileExtsn = orignlFileNm.substring(orignlFileNm.lastIndexOf(".") + 1);
      // 저장 파일명
      String streFileNm =
          getUniqueFileNm(dateTimeFormat.format(currentDate) + "." + fileExtsn, basePath);
      // 파일 사이즈
      Long fileSize = file.getSize();

      // 파일 업로드
      Path filePath = Paths.get(basePath, streFileNm);
      try {
        Files.write(filePath, file.getBytes());
      } catch (IOException e) {
        e.printStackTrace();
        logger.error("[fileUtil.uploadFile] : " + e.toString());
        continue;
      }

      // createDTO 설정
      FileCreateDTO createDTO = FileCreateDTO
          .builder()
          .fileSeq(param.getFileSeq())
          .fileSn(fileSn)
          .fileStrePath(subDir)
          .streFileNm(streFileNm)
          .orignlFileNm(orignlFileNm)
          .fileExtsn(fileExtsn)
          .fileSize(fileSize)
          .rgtrSysId(param.getRgtrSysId())
          .mdfrSysId(param.getMdfrSysId())
          .build();

      FileDtl fileDtl = createDTO.toEntity();
      fileDtl.create();

      fileList.add(fileDtl);

      fileSn++;
    }

    return fileList;
  }

  /**
   * 저장 파일명 취득(유니크)
   * 
   * @param fileNm
   * @param basePath
   * @return
   */
  public String getUniqueFileNm(String fileNm, String basePath) {
    String uFileNm = fileNm;

    File file = new File(basePath, fileNm);
    if (!file.exists())
      return uFileNm;

    int cnt = 1;
    String baseNm = uFileNm.substring(0, uFileNm.lastIndexOf("."));
    String extsn = uFileNm.substring(uFileNm.lastIndexOf("."));
    do {
      uFileNm = baseNm + cnt + extsn;
      file = new File(basePath, uFileNm);
      cnt++;
    } while (file.exists());

    return uFileNm;
  }

  /**
   * 파일 다운로드
   * 
   * @param fileStrePath
   * @param streFileNm
   * @return ResponseEntity<Resource>
   * @throws Exception
   */
  public ResponseEntity<Resource> downloadFile(String fileStrePath, String streFileNm,
      String orignlFileNm) throws Exception {

    // 파일 경로 설정
    String filePath = UPLOAD_PATH + fileStrePath + streFileNm;

    Path path = Paths.get(filePath);
    Resource resource = new InputStreamResource(Files.newInputStream(path));

    if (resource.exists() && resource.isReadable()) {
      HttpHeaders headers = new HttpHeaders();
      headers
          .add(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=" + URLEncoder.encode(orignlFileNm, "UTF-8"));
      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

      return ResponseEntity.ok().headers(headers).body(resource);

    } else {
      logger.error("[fileUtil.downloadFile] NOT_FOUND : " + filePath);
      return ResponseEntity.notFound().build();
    }
  }
}
