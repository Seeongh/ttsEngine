package com.kosaf.core.api.frequencyPhrases.presentation;

import com.kosaf.core.api.bbs.application.dto.BbsListParam;
import com.kosaf.core.api.frequencyPhrases.application.dto.*;
import com.kosaf.core.api.frequencyPhrases.application.FrequencyPhrasesService;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.config.validation.ValidSequence;
import lombok.Getter;
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
public class FrequencyPhrasesRestController {

    private final FrequencyPhrasesService frequencyPhService;

    private static final Logger log = LoggerFactory.getLogger(FrequencyPhrasesRestController.class);

    /**
     * 대체 키워드 List
     * @param param
     * @return
     */
    @GetMapping("/phrases")
    public ApiResponse<PageResult<FrequencyPhListDTO>> findAll(@ModelAttribute FrequencyPhListParam param) {
        return frequencyPhService.findAll(param);
    }


    /**
     * 키워드 상세 가져오기
     */
    @GetMapping("/phrases/{phraseSeq}")
    public ApiResponse<FrequencyPhDetailDTO> findById(@PathVariable @Min(0) int phraseSeq) {
        return frequencyPhService.findById(phraseSeq);
    }

    /**
     * 키워드 등록 및 중복 여부 체크
     * @return
     */
    @PostMapping("/phrases")
    public ApiResponse<Long> create(@ModelAttribute @Validated(ValidSequence.class) FrequencyPhCreateDTO createDto) {

        return frequencyPhService.create(createDto);
    }


    /**
     * 키워드 등록 및 중복 여부 체크
     * @return
     */
    @PatchMapping("/phrases")
    public ApiResponse<FrequencyPhDetailDTO> update(@ModelAttribute FrequencyPhUpdateDTO updateDto) {
        return frequencyPhService.update(updateDto);
    }

    /**
     * 엑셀 파일
     */
    @PostMapping("/checkFileFP")
    public ApiResponse<List<InputPhrasesDTO>> checkFileFP (MultipartFile excelfile) {
        return frequencyPhService.checkKwFile(excelfile);
    }

    /**
     * 엑셀파일 포맷 다운로드
     * @param param
     * @param req
     * @param res
     */
    @GetMapping("/frequencyPh/excel-down")
    public void excelDown(HttpServletRequest req, HttpServletResponse res) {

        frequencyPhService.excelDown( req, res);
    }

    /**
     * 엑셀 내려받기
     */
    @GetMapping("/frequencyPh/excel-list")
    public void excelList(FrequencyPhListParam param, HttpServletRequest req, HttpServletResponse res) {
        frequencyPhService.resultExcleDown(param,req,res);
    }
}
