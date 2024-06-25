package com.kosaf.core.api.replaceKeyword.presentation;

import com.kosaf.core.api.bbs.application.dto.BbsListParam;
import com.kosaf.core.api.replaceKeyword.application.ReplaceKeywordService;
import com.kosaf.core.api.replaceKeyword.application.dto.*;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.config.validation.ValidSequence;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.util.List;

/* @Validated
*            "@PathVariable" "@RequestParam" 으로 받는 객체에 javax.validation.constraints 사용을 위해 추가.<br>
*            "@RequestBody" 에 "@Validated" 사용하는 경우에는 추가할 필요 없음.
 */

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplaceKeywordRestController {

    private final ReplaceKeywordService replaceKwService;

    private static final Logger log = LoggerFactory.getLogger(ReplaceKeywordRestController.class);

    /**
     * 대체 키워드 List
     * @param param
     * @return
     */
    @GetMapping("/keyword")
    public ApiResponse<PageResult<ReplaceKwListDTO>> findAll(@ModelAttribute ReplaceKwListParam param ) {
        return replaceKwService.findAll(param);
    }


    /**
     * 키워드 상세 가져오기
     */
    @GetMapping("/keyword/{rKeywordSeq}")
    public ApiResponse<ReplaceKwDetailDTO> findById(@PathVariable @Min(0) int rKeywordSeq) {
        return replaceKwService.findById(rKeywordSeq);
    }

    /**
     * 키워드 등록 및 중복 여부 체크
     * @return
     */
    @PostMapping("/keyword")
    public ApiResponse<Long> create(@ModelAttribute @Validated(ValidSequence.class) ReplaceKwCreateDTO createDto) {

        return replaceKwService.create(createDto);
    }


    /**
     * 키워드 등록 및 중복 여부 체크
     * @return
     */
    @PatchMapping("/keyword")
    public ApiResponse<ReplaceKwDetailDTO> update(@ModelAttribute ReplaceKwUpdateDTO updateDto) {
        return replaceKwService.update(updateDto);
    }



    /**
     * 엑셀 파일
     */
    @PostMapping("/checkFile")
    public ApiResponse<List<InputKeywordDTO>> checkFile (MultipartFile excelfile) {
        log.info("ash excelfile = {}", excelfile.toString());
        return replaceKwService.checkKwFile(excelfile);
    }

    /**
     * 엑셀파일 다운로드
     * @param param
     * @param req
     * @param res
     */
    @GetMapping("/replacekeyword/excel-down")
    public void excelDown(HttpServletRequest req, HttpServletResponse res) {

        replaceKwService.excelDown(req, res);
    }


    /**
     * 대체키워드 리스트 다운로드
     * @param param
     * @param req
     * @param res
     */
    @GetMapping("/replacekeyword/excel-list")
    public void excelList(ReplaceKwListParam param, HttpServletRequest req, HttpServletResponse res) {

        replaceKwService.resultExcleDown(param, req, res);
    }

}
