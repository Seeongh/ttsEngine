package com.kosaf.core.api.replaceKeyword.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kosaf.core.api.bbs.application.dto.BbsDTO;
import com.kosaf.core.api.bbs.application.dto.BbsListParam;
import com.kosaf.core.api.frequencyPhrases.application.dto.FrequencyPhCreateDTO;
import com.kosaf.core.api.frequencyPhrases.application.dto.FrequencyPhListParam;
import com.kosaf.core.api.frequencyPhrases.domain.FrequencyPh;
import com.kosaf.core.api.replaceKeyword.application.dto.*;
import com.kosaf.core.api.replaceKeyword.domain.ReplaceKw;
import com.kosaf.core.api.replaceKeyword.infrastructure.ReplaceKeywordMapper;
import com.kosaf.core.api.replaceKeyword.value.UseFilter;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.ExcelUtil;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.common.ResponseException;
import com.kosaf.core.config.validation.ValidSequence;
import com.kosaf.core.config.webClient.ServerCaller;
import com.kosaf.core.config.webClient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Validated(ValidSequence.class)
@Transactional
public class ReplaceKeywordService {

    private final ReplaceKeywordMapper replaceKwMapper;
    private final ServerCaller serverCaller;

    /**
     * 엑셀 헤드
     */
    private final String Excel_column1 = "순서";
    private final String Excel_column2 = "메인키워드";
    private final String Excel_column3 = "대체키워드";

    public ApiResponse<PageResult<ReplaceKwListDTO>> findAll(ReplaceKwListParam param) {

        try {

            PageResult<ReplaceKwListDTO> list = PageResult
                    .<ReplaceKwListDTO>builder()
                    .page(param.getPage()) //요청된 페이지 번호 설정
                    .pageScale(param.getPageScale())
                    .totalCount(replaceKwMapper.countAll(param))
                    .items(Optional
                            .ofNullable(replaceKwMapper.findAll(param))
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .map(ReplaceKwListDTO::of)
                            .collect(Collectors.toList()))
                    .build();

            return list.getItems().isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
                    : ApiResponse.of(HttpStatus.OK, list);
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public ApiResponse<ReplaceKwDetailDTO> findById(int rKeywordSeq) {
        try {
            ApiResponse<ReplaceKw> replaceKwResponse = memberInfoById(rKeywordSeq);

            return Optional
                    .ofNullable(replaceKwResponse.getData())
                    .map(replaceKw -> ApiResponse.of(HttpStatus.OK, ReplaceKwDetailDTO.of(replaceKw)))
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
    public ApiResponse<Long> create(ReplaceKwCreateDTO createDto) {
        if(createDto == null) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
        }

        Long result = 0L;
        if((createDto.getKeywordDtoList() != null) && (!createDto.getKeywordDtoList().isEmpty())) { //엑셀을 통해 여러개가 들어옴
            result = handleMultipleInsert(createDto);
        }
        else {
            result = handleSingleInsert(createDto);
        }

        try {
            return result > 0L ? ApiResponse.of(HttpStatus.CREATED, result)
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
    private Long handleMultipleInsert(ReplaceKwCreateDTO createDto) {
        Long result = 0L;
        List<ReplaceKwCreateDTO> duplicates = new ArrayList<>();
        List<String> uniqueList = new ArrayList<>();

        for (ReplaceKwCreateDTO dto : createDto.getKeywordDtoList()) {
            if (isDuplicatedKeywoard(dto.getMainKw())) {
                log.info("duplicated = {}", dto.getMainKw());
                duplicates.add(dto);
            }
            else { //엑셀내 중복 확인
                uniqueList.add(dto.getMainKw());
            }
        }

        if(!duplicates.isEmpty()) {
            // 중복된 항목 제거
            log.info("ash duplicated removeall");
            createDto.getKeywordDtoList().removeAll(duplicates);
        }

        // 리스트 내 어떤 기준으로 중복을 제거해야할지 모름
        Set<String> set = new HashSet<>(uniqueList); // 중복 제거를 위해 HashSet에 넣음
        if (set.size() < createDto.getKeywordDtoList().size()) {
            throw new ResponseException("중복된 항목이 있습니다. 제거 해주세요.", HttpStatus.BAD_REQUEST);
        }

        // 전부 중복시 exception
        if( createDto.getKeywordDtoList().isEmpty()) {
            throw new ResponseException(HttpStatus.CONFLICT);
        }

        result = insertReplaceKw(createDto);
        return result;
    }

    /**
     * 단일 insert handler
     * @param createDto
     * @return
     */
    private Long handleSingleInsert(ReplaceKwCreateDTO createDto) {
        if (isDuplicatedKeywoard(createDto.getMainKw())) {
            throw new ResponseException(HttpStatus.CONFLICT);
        }
        return insertReplaceKw(createDto);
    }

    /**
     * 진짜 insert수행
     * @param createDto
     * @return
     */
    private Long insertReplaceKw(ReplaceKwCreateDTO createDto ) {

        try {

            if((createDto.getKeywordDtoList() != null) && (!createDto.getKeywordDtoList().isEmpty())) { //엑셀을 통해 여러개가 들어옴
                for(ReplaceKwCreateDTO dto : createDto.getKeywordDtoList()) {
                    ReplaceKw replaceKw = dto.toEntity(); // dto -> entity
                    replaceKw.create();

                    replaceKwMapper.create(replaceKw);
                }
            }
            else {
                ReplaceKw replaceKw = createDto.toEntity(); // dto -> entity
                replaceKw.create();

                replaceKwMapper.create(replaceKw);
            }
        }
        catch (Exception e) {
            throw new ResponseException("예기치 못한 에러가 발생했습니다. 포맷을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }

        //서버에 request 보냄
        Long requestResult = serverCaller.requestReplaceKwServer(createDto, RequestCommandMethod.insert.name(), "/word-replace","/word-replace");
        if(requestResult == 0L) {
            throw new ResponseException("오류로 인해 등록 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return requestResult;
    }

    /**
     * insert 중복 검사
     * @param keyword
     */
    private boolean isDuplicatedKeywoard(String keyword) {
        return replaceKwMapper.findByString(keyword) != null; //값이 있으면 중복 true, 없으면 false
    }

    /**
     * 대체어 수정
     * @param updateDto
     * @return
     */
    public ApiResponse<ReplaceKwDetailDTO> update(@Valid ReplaceKwUpdateDTO updateDto) {
        if (updateDto == null) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, null) ;
        }

        Long result = 0L;
        ReplaceKw replaceKw = updateDto.toEntity();
        replaceKw.update();

        //변경된 엔티티로 적합성 검사
        boolean requestValid = checkValidationKeyword(replaceKw);

        ReplaceKwDetailDTO updatedReplaceKwDetail = ReplaceKwDetailDTO.of(replaceKw);

        //기본 전략 - 삽입
        String cmdMethod = RequestCommandMethod.insert.name();

        if(requestValid) { //사용 여부가 변경되어 서버로 Request전달
            if (updateDto.getUseAt() == UseFilter.N) {
                cmdMethod = RequestCommandMethod.delete.name();
            }
        }

        try {
            result +=  replaceKwMapper.update(replaceKw);
        }
        catch (Exception e) {
            throw new ResponseException("예기치 못한 에러가 발생했습니다. 포맷을 확인해주세요.", HttpStatus.BAD_REQUEST);
        }

        Long  requestResult  = serverCaller.requestReplaceKwServer(new ReplaceKwCreateDTO(updateDto.getMainKw(), updateDto.getReplaceKw()), cmdMethod,"/word-replace","/word-replace");
        if(requestResult == 0L) {

            log.info("ash useAt update = {},{},{}" , updateDto.getMainKw(), updateDto.getReplaceKw(), updateDto.getUseAt());
            throw new ResponseException("오류로 인해 등록 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

     return result > 0 ? ApiResponse.of(HttpStatus.OK, updatedReplaceKwDetail)
            : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);

    }

    public ApiResponse<ReplaceKw> memberInfoById(int rkeywordSeq) {

        try {
            Optional<ReplaceKw>  replaceKwById = Optional.ofNullable(replaceKwMapper.findById(rkeywordSeq));

            return replaceKwById
                    .map(replaceKw -> ApiResponse.of(HttpStatus.OK, replaceKw))
                    .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    /**
     * update 데이터 적합성 검사
     */
    private boolean checkValidationKeyword(ReplaceKw replaceKw) {
        //가지고온 데이터와 비교해서 다음 두상황은 잘못된 요청으로 판단.
        // 1. 데이터 없음
        ReplaceKw fetchedReplaceKw = Optional.ofNullable(memberInfoById(replaceKw.getRkeywordSeq()).getData())
                .orElseThrow(() -> new ResponseException(HttpStatus.NOT_FOUND));

        // 2. 메인 키워드 변경감지
        if (!fetchedReplaceKw.getMainKw().equals(replaceKw.getMainKw())) {
            throw new ResponseException(HttpStatus.BAD_REQUEST);
        }

        if(!fetchedReplaceKw.getUseAt().equals(replaceKw.getUseAt())) {
            //변경되었다고 판단.
            return true;
        }

        return false;

    }
    public ApiResponse<List<InputKeywordDTO>> checkKwFile(MultipartFile file) {
        List<InputKeywordDTO> dataList = new ArrayList<>();
        Boolean isDup = false;
        try {
            Sheet worksheet = getRows(file);

            //헤더가 예상한 포맷이 아닌경우 exception
            Row head= worksheet.getRow(0);
            if(!(head.getCell(0).getStringCellValue().equals(Excel_column1)) ||
                    !(head.getCell(1).getStringCellValue().equals(Excel_column2)) ||
                    !(head.getCell(2).getStringCellValue().equals(Excel_column3))) {

                throw new ResponseException(HttpStatus.BAD_REQUEST);
            }

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { //body


                Row row = worksheet.getRow(i);
                if(row.getCell(1).getStringCellValue().isEmpty() || row.getCell(1).getStringCellValue().equals(" ")) {
                    continue;
                }

                InputKeywordDTO keywordDTO = InputKeywordDTO
                        .builder()
                        .num((int)row.getCell(0).getNumericCellValue())
                        .mainKw(row.getCell(1).getStringCellValue())
                        .replaceKw(row.getCell(2).getStringCellValue())
                        .duplicated(
                                Optional.
                                        ofNullable(replaceKwMapper.findByString(row.getCell(1).getStringCellValue()))
                                        .isPresent() //값이있으면 중복으로 true
                        )
                        .build();

                dataList.add(keywordDTO);
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
     * 대체키워드 양식 리스트 엑셀 다운로드
     *
     * @param param
     * @param req
     * @param res
     */
    public void excelDown(HttpServletRequest req, HttpServletResponse res) {
        String headNms = Excel_column1+","+Excel_column2+","+Excel_column3;
        String fields = ",,";

        ExcelUtil excelUtil = new ExcelUtil("대체키워드_입력_포맷", headNms, fields, req, res);
        excelUtil.setDataToExcelList(new ArrayList<>(), "입력 포맷");
        excelUtil.writeExcel();
    }

    /**
     * 대체 키워드 리스트 엑셀 다운로드
     * @param param
     * @param req
     * @param res
     */
    public void resultExcleDown(ReplaceKwListParam param, HttpServletRequest req, HttpServletResponse res) {
        //전체 출력하려고 파람값 안넣음
        ReplaceKwListParam temp = ReplaceKwListParam.builder().build();
        List<ReplaceKw> list = replaceKwMapper.findAll(temp);

        String headNms = "순번,메인키워드,대체키워드,사용여부,등록일,수정일,수정자";
        String fields = "replaceKwNo,mainKw,replaceKw,useAt,registDt,updtDt,registId";

        ExcelUtil excelUtil = new ExcelUtil("대체키워드_목록_리스트", headNms, fields, req, res);
        excelUtil.setDataToExcelList(list, "목록");
        excelUtil.writeExcel();
    }


}

