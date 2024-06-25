package com.kosaf.core.api.file.presentation;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.Min;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.kosaf.core.api.file.application.FileService;
import com.kosaf.core.api.file.application.dto.FileCreateDTO;
import com.kosaf.core.api.file.application.dto.FileDTO;
import com.kosaf.core.api.file.application.dto.FileListParam;
import com.kosaf.core.api.file.application.dto.FileUpdateDTO;
import com.kosaf.core.common.ApiResponse;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileRestController {

  // RestAPI Controller

  private final FileService fileService;

  @GetMapping("/multipart")
  public ApiResponse<Map<String, Object>> getMultipartConfig() {

    return fileService.getMultipartConfig();
  }

  @GetMapping("/files")
  public ApiResponse<List<FileDTO>> findAll(FileListParam param) {

    return fileService.findAll(param);
  }

  @GetMapping("/file/{fileDtlSeq}")
  public ResponseEntity<Resource> findById(
      @PathVariable @Min(value = 0, message = "요청이 올바르지 않습니다.") Long fileDtlSeq) {

    return fileService.findById(fileDtlSeq);
  }

  @PostMapping("/file")
  public ApiResponse<Long> create(@ModelAttribute FileCreateDTO createDTO,
      MultipartHttpServletRequest multiRequest) {

    return fileService.create(createDTO, multiRequest);
  }

  @PatchMapping("/file")
  public ApiResponse<Long> update(@ModelAttribute FileUpdateDTO updateDTO,
      MultipartHttpServletRequest multiRequest) {

    return fileService.update(updateDTO, multiRequest);
  }

}
