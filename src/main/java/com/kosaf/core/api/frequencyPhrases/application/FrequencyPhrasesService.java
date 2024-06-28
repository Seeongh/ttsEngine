package com.kosaf.core.api.frequencyPhrases.application;

import com.kosaf.core.api.frequencyPhrases.application.dto.*;
import com.kosaf.core.api.frequencyPhrases.domain.FrequencyPh;
import com.kosaf.core.api.frequencyPhrases.infrastructure.FrequencyPhrasesMapper;
import com.kosaf.core.api.frequencyPhrases.value.UseFilter;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.ExcelUtil;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.common.exception.ResponseException;
import com.kosaf.core.config.validation.ValidSequence;
import com.kosaf.core.config.webClient.ServerCaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Validated(ValidSequence.class)
@Transactional
public class FrequencyPhrasesService {

    private final FrequencyPhrasesMapper frequencyPhMapper;
    private final ServerCaller serverCaller;


    /**
     * 엑셀 헤드
     */
    private final String Excel_column1 = "순서";
    private final String Excel_column2 = "자주 사용하는 문구";

    public ApiResponse<PageResult<FrequencyPhListDTO>> findAll(FrequencyPhListParam param) {

        try {
            PageResult<FrequencyPhListDTO> list = PageResult
                    .<FrequencyPhListDTO>builder()
                    .page(param.getPage()) //요청된 페이지 번호 설정
                    .pageScale(param.getPageScale())
                    .totalCount(frequencyPhMapper.countAll(param))
                    .items(Optional
                            .ofNullable(frequencyPhMapper.findAll(param))
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .map(FrequencyPhListDTO::of)
                            .collect(Collectors.toList()))
                    .build();

            return list.getItems().isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
                    : ApiResponse.of(HttpStatus.OK, list);
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public ApiResponse<FrequencyPhDetailDTO> findById(int phraseSeq) {
        try {
            ApiResponse<FrequencyPh> replaceKwResponse = memberInfoById(phraseSeq);

            return Optional
                    .ofNullable(replaceKwResponse.getData())
                    .map(replaceKw -> ApiResponse.of(HttpStatus.OK, FrequencyPhDetailDTO.of(replaceKw)))
                    .orElse(ApiResponse.of(HttpStatus.valueOf(replaceKwResponse.getStatus()), null));
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    /**
     * insert
     * @param createDto
     * @return
     */
    public ApiResponse<Long> create(FrequencyPhCreateDTO createDto) {
        if(createDto == null) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
        }
        Long result = 0L;

        if((createDto.getFpDtoList() != null) && (!createDto.getFpDtoList().isEmpty())) { //엑셀을 통해 여러개가 들어옴
                result = handleMultipleInsert(createDto);
        }
        else {
            result = handleSingleInsert(createDto);
        }

        try {
            return result > 0 ? ApiResponse.of(HttpStatus.CREATED, result)
                    : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }


    /**
     * 리스트 insert handler
     * @param keywordDtoList
     * @return
     */
    private Long handleMultipleInsert(FrequencyPhCreateDTO createDto) {
        Long result = 0L;
        List<FrequencyPhCreateDTO> duplicates = new ArrayList<>();
        List<String> uniqueList = new ArrayList<>();
        for (FrequencyPhCreateDTO dto : createDto.getFpDtoList()) {
            if (isDuplicatedFrequencyPh(dto.getFrequencyPh())) {
                duplicates.add(dto);
            }
            else {
                uniqueList.add(dto.getFrequencyPh());
            }
        }

        // DB에서 중복된 항목 제거
        if(!duplicates.isEmpty()) {
            createDto.getFpDtoList().removeAll(duplicates);
        }

        // 리스트 내 중복 제거
        Set<String> set = new HashSet<>(uniqueList); // 중복 제거를 위해 HashSet에 넣음
        if (set.size() < createDto.getFpDtoList().size()) {
            throw new ResponseException("중복된 항목이 있습니다. 제거 해주세요.", HttpStatus.BAD_REQUEST);
        }

        // 전부 중복시 exception
        if(createDto.getFpDtoList().isEmpty()) {
            throw new ResponseException(HttpStatus.CONFLICT);
        }
        result =  insertFrequencyPh(createDto);
        return result;
    }

    /**
     * 단일 insert handler
     * @param createDto
     * @return
     */
    private Long handleSingleInsert(FrequencyPhCreateDTO createDto) {
        if (isDuplicatedFrequencyPh(createDto.getFrequencyPh())) {
            throw new ResponseException(HttpStatus.CONFLICT);
        }
        return insertFrequencyPh(createDto);
    }

    /**
     * 진짜 insert수행
     * @param createDto
     * @return
     */

    private Long insertFrequencyPh(FrequencyPhCreateDTO createDto) {


        try {
            if((createDto.getFpDtoList() != null) && (!createDto.getFpDtoList().isEmpty())) { //엑셀을 통해 여러개가 들어옴
                for(FrequencyPhCreateDTO dto : createDto.getFpDtoList()) {
                    FrequencyPh frequencyPh = dto.toEntity(); // dto -> entity
                    frequencyPh.create();
                    frequencyPhMapper.create(frequencyPh);
                }
            }
            else {
                FrequencyPh frequencyPh = createDto.toEntity(); // dto -> entity
                frequencyPh.create();
                frequencyPhMapper.create(frequencyPh);
            }

        }
        catch(Exception e) {
            throw new ResponseException("예기치 못한 에러가 발생했습니다. 포맷을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }

        Long requestResult = serverCaller.requestCacheFrequencyPh(createDto, RequestCacheMethod.WRITE.name()); //cache에 저장
        if(requestResult == 0L) {
            throw new ResponseException("오류로 인해 등록 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return requestResult;
    }

    /**
     * insert 중복 검사
     * @param frequencyPhstr
     */
    private boolean isDuplicatedFrequencyPh(String frequencyPhstr) {
        return frequencyPhMapper.findByString(frequencyPhstr) != null; //값이 있으면 중복 true, 없으면 false
    }


    public ApiResponse<FrequencyPhDetailDTO> update(@Valid FrequencyPhUpdateDTO updateDto) {
        if (updateDto == null) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, null) ;
        }

        FrequencyPh frequencyPh = updateDto.toEntity();
        frequencyPh.update();

        //변경된 엔티티로 적합성 검사x ->
        boolean requestValid = checkValidationFrequencyPh(frequencyPh);
        Long result = 0L;
        FrequencyPhDetailDTO updatedReplaceKwDetail = FrequencyPhDetailDTO.of(frequencyPh);

        String cacheMethod = "";

        //기본 전략 - 사용
        cacheMethod = RequestCacheMethod.WRITE.name();

        if(requestValid) { //사용 여부 YES-NO 변경
            if(updateDto.getUseAt() == UseFilter.N) {
                log.info("전략 변경 cache = {}", RequestCacheMethod.OFF.name());
                cacheMethod = RequestCacheMethod.OFF.name();
            }
        }

        try {
            result += frequencyPhMapper.update(frequencyPh);
        }
        catch (Exception e) {
            throw new ResponseException("예기치 못한 에러가 발생했습니다. 포맷을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }


        Long  requestResult =  serverCaller.requestCacheFrequencyPh(new FrequencyPhCreateDTO(updateDto.getFrequencyPh()), cacheMethod); //cache에 저장
        if(requestResult == 0L) {
            log.info("ash update result : {}", requestResult);
            throw new ResponseException("오류로 인해 등록 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result > 0 ? ApiResponse.of(HttpStatus.OK, updatedReplaceKwDetail)
                : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);

    }


    private boolean checkValidationFrequencyPh(FrequencyPh frequencyPh) {
        // 1. 데이터 없음
        FrequencyPh fetchedFrequencyPh = Optional.ofNullable(memberInfoById(frequencyPh.getPhraseSeq()).getData())
                .orElseThrow(() -> new ResponseException(HttpStatus.NOT_FOUND));

        if(!fetchedFrequencyPh.getUseAt().equals(frequencyPh.getUseAt())) {
            //사용여부가 변경되었다고 판단.
            return true;
        }
        return false;
    }
    public ApiResponse<FrequencyPh> memberInfoById(int phraseSeq) {

        try {
            Optional<FrequencyPh>  frequencyPhById = Optional.ofNullable(frequencyPhMapper.findById(phraseSeq));

            return frequencyPhById
                    .map(FrequencyPh -> ApiResponse.of(HttpStatus.OK, FrequencyPh))
                    .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public ApiResponse<List<InputPhrasesDTO>> checkKwFile(MultipartFile file) {
        List<InputPhrasesDTO> dataList = new ArrayList<>();
        Boolean isDup = false;

        try {
            Sheet worksheet = getRows(file);

            //헤더가 예상한 포맷이 아닌경우 exception
            Row head= worksheet.getRow(0);
            if(!(head.getCell(0).getStringCellValue().equals(Excel_column1)) ||
                    !(head.getCell(1).getStringCellValue().equals(Excel_column2))) {
                throw new ResponseException(HttpStatus.BAD_REQUEST);
            }

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 3

                Row row = worksheet.getRow(i);
                if(row.getCell(1).getStringCellValue().isEmpty() || row.getCell(1).getStringCellValue().equals(" ")) {
                    continue;
                }

                InputPhrasesDTO FrequencyPh = InputPhrasesDTO
                        .builder()
                        .num((int)row.getCell(0).getNumericCellValue())
                        .frequencyPh(row.getCell(1).getStringCellValue())
                        .duplicated(
                                Optional.
                                        ofNullable(frequencyPhMapper.findByString(row.getCell(1).getStringCellValue()))
                                        .isPresent() //값이있으면 중복으로 true
                        )
                        .build();

                dataList.add(FrequencyPh);
            }

            file.getInputStream().close();
            return ApiResponse.of(HttpStatus.OK, dataList);
        }
        catch (Exception e) {
            throw new ResponseException("파일에 문제가 있습니다. 다시 확인해주세요.", HttpStatus.BAD_REQUEST);
        }
        finally {
        }

    }

    private static Sheet getRows(MultipartFile file) throws IOException {
        if (file == null) {
            throw new ResponseException(HttpStatus.BAD_REQUEST);
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if(extension == null) {
            throw  new ResponseException(HttpStatus.BAD_REQUEST);
        }
        if(!(extension.equals("xlsx") || extension.equals("xls") || extension.equals("csv"))) {
            throw  new ResponseException(HttpStatus.NOT_ACCEPTABLE);
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);
        return worksheet;
    }

    /**
     * 자주 사용하는 문구 양식 리스트 엑셀 다운로드
     *
     * @param param
     * @param req
     * @param res
     */
    public void excelDown(HttpServletRequest req, HttpServletResponse res) {
        log.info("excel down ");
        String headNms = Excel_column1+","+Excel_column2;
        String fields = ",";

        ExcelUtil excelUtil = new ExcelUtil("자주사용하는문구_입력_포맷", headNms, fields, req, res);
        excelUtil.setDataToExcelList(new ArrayList<>(), "입력 포맷");
        excelUtil.writeExcel();
    }


    /**
     * 자주사용하는 리스트 엑셀 다운로드
     *
     * @param param
     * @param req
     * @param res
     */
    public void resultExcleDown(FrequencyPhListParam param, HttpServletRequest req, HttpServletResponse res) {
        //전체 출력하려고 파람값 안넣음
        FrequencyPhListParam temp= FrequencyPhListParam.builder().build();
        List<FrequencyPh> list = frequencyPhMapper.findAll(temp);

        String headNms = "순번,자주사용하는문구,사용여부,등록일,수정일,수정자";
        String fields = "phraseNo,frequencyPh,useAt,registDt,updtDt,registId";

        ExcelUtil excelUtil = new ExcelUtil("자주_사용하는_목록", headNms, fields, req, res);
        excelUtil.setDataToExcelList(list, "목록");
        excelUtil.writeExcel();
    }
}

