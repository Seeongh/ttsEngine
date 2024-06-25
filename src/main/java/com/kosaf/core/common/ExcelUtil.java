package com.kosaf.core.common;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelUtil {

  private final Log logger = LogFactory.getLog(getClass());

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
  private final static short DEFAULT_HEIGHT = 400;
  private final static Integer DEFAULT_WIDTH = 4000;

  private HttpServletRequest req;
  private HttpServletResponse res;
  protected HSSFSheet[] sheetArray;
  protected HSSFWorkbook wb;
  protected String fileNm;
  protected String[] headNms;
  protected String[] fields;
  protected CellStyle headStyle;
  protected CellStyle bodyStyle;

  /**
   * excel util 생성자
   * 
   * @param fileNm 생성할 엑셀 파일명
   * @param headNm 엑셀 파일의 행 제목들을 쉼표로 구분한 문자열
   * @param fields 엑셀 파일의 필드들을 쉼표로 구분한 문자열
   * @param request HTTP 요청 객체
   * @param response HTTP 응답 객체
   */
  public ExcelUtil(String fileNm, String headNm, String field, HttpServletRequest request,
      HttpServletResponse response) {

    this.req = request;
    this.res = response;
    this.wb = new HSSFWorkbook();
    this.fileNm = fileNm;
    this.headNms = headNm.split(",");
    this.fields = field.split(",");
    this.headStyle = createHeadStyle();
    this.bodyStyle = createBodyStyle();
    // response header 설정
    setResponseHeaders();
  }

  /**
   * 데이터 시트로 변환 (데이터를 시트에 입력)
   * 
   * @param data 엑셀에 입력할 데이터 목록
   * @param sheetNm 생성할 시트명
   * @return HSSFSheet
   */
  public HSSFSheet setDataToExcelList(List<?> data, String sheetNm) {

    HSSFSheet st = null;

    try {
      st = wb.createSheet(sheetNm);

      // head 설정
      HSSFRow titleRow = st.createRow(0);
      titleRow.setHeight(DEFAULT_HEIGHT);
      setHeadData(st, titleRow);

      // 데이터 삽입
      for (int i = 0; i < data.size(); i++) {
        HSSFRow row = st.createRow(st.getLastRowNum() + 1);
        row.setHeight(DEFAULT_HEIGHT);
        setRowData(row, data.get(i));
      }

      // 셀 크기 조정
      for (int i = 0; i < fields.length; i++) {
        st.autoSizeColumn(i);
        st.setColumnWidth(i, Math.max(st.getColumnWidth(i), DEFAULT_WIDTH));
      }
    } catch (Exception e) {
      logger.error("[excelUtil.setDataToExcelList] Exception : " + e.getMessage());
    }

    return st;
  }

  /**
   * 시트 head 설정
   * 
   * @param st 시트
   * @param title 제목을 포함하는 행
   */
  private void setHeadData(HSSFSheet st, HSSFRow title) {

    for (int i = 0; i < headNms.length; i++) {
      title.createCell(i).setCellValue(headNms[i]);
      title.getCell(i).setCellStyle(headStyle);
    }
  }

  /**
   * 행 데이터 설정
   * 
   * @param row 데이터가 입력될 행
   * @param data 입력할 데이터
   * @return HSSFRow
   * @throws Exception
   */
  private void setRowData(HSSFRow row, Object data) {

    try {
      // 각 필드에 대한 데이터 삽입
      for (int i = 0; i < fields.length; i++) {
        String value = getValue(data, fields[i]);
        row.createCell(i).setCellValue(value);
        row.getCell(i).setCellStyle(bodyStyle);
      }
    } catch (Exception e) {
      logger.error("[excelUtil.setRowData] Exception : " + e.getMessage());
    }
  }

  /**
   * 객체 필드 값 취득
   * 
   * @param data 입력할 데이터
   * @param fieldNm 데이터 필드명
   * @return String
   * @throws NoSuchFieldException
   * @throws IllegalAccessException
   */
  private String getValue(Object data, String fieldNm)
      throws NoSuchFieldException, IllegalAccessException {

    Class<?> currentClass = data.getClass();
    Field field;

    // 객체의 필드명을 통해 값 취득
    while (currentClass != null) {
      try {
        field = currentClass.getDeclaredField(fieldNm);
        field.setAccessible(true);
        Object value = field.get(data);
        return value != null ? value.toString() : "";
      } catch (NoSuchFieldException e) {
        currentClass = currentClass.getSuperclass();
      }
    }

    return "";
  }

  /**
   * response header 설정
   */
  private void setResponseHeaders() {

    try {
      // 파일 제목 인코딩
      String encodedFileNm = URLEncoder.encode(fileNm + ".xls", "UTF-8");

      res.setHeader("Content-Disposition", "attachment; filename=" + encodedFileNm);
      res.setContentType("application/vnd.ms-excel");

    } catch (Exception e) {
      logger.error("[excelUtil.downloadExcel] Exception : " + e.getMessage());
    }
  }

  /**
   * 엑셀 파일 작성
   */
  public void writeExcel() {

    try (ServletOutputStream sos = res.getOutputStream()) {
      wb.write(sos);
      sos.flush();
    } catch (Exception e) {
      logger.error("[excelUtil.writeExcel] Exception : " + e.getMessage());
    }
  }

  /**
   * 기본 head style 설정
   * 
   * @return CellStyle
   */
  private CellStyle createHeadStyle() {

    HSSFFont font = wb.createFont();
    font.setBold(true);
    font.setColor(IndexedColors.WHITE.index);
    CellStyle style = wb.createCellStyle();
    style.setFont(font);
    style.setWrapText(true);
    style.setFillForegroundColor(IndexedColors.ROYAL_BLUE.index);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    return style;
  }

  /**
   * 기본 body style 설정
   * 
   * @return CellStyle
   */
  private CellStyle createBodyStyle() {

    HSSFFont font = wb.createFont();
    CellStyle style = wb.createCellStyle();
    style.setFont(font);
    style.setWrapText(true);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    return style;
  }

  /**
   * WorkBook 반환
   * 
   * @return HSSFWorkbook
   */
  public HSSFWorkbook getHSSFWorkbook() {
    return wb;
  }

  /**
   * headStyle 설정
   * 
   * @param headStyle
   */
  public void setHeadStyle(CellStyle headStyle) {
    this.headStyle = headStyle;
  }

  /**
   * bodyStyle 설정
   * 
   * @param bodyStyle
   */
  public void setBodyStyle(CellStyle bodyStyle) {
    this.bodyStyle = bodyStyle;
  }

  /**
   * 엑셀 시트 순서 변경
   * 
   * @param index 변경할 시트 순서
   * @param st 시트 데이터
   */
  public void setSheetOrder(int index, HSSFSheet st) {

    if (index >= 0 && index < wb.getNumberOfSheets())
      wb.setSheetOrder(st.getSheetName(), index);
    else
      throw new IllegalArgumentException("[excelUtil.setSheetOrder] Index out of bounds");
  }

  /**
   * 엑셀 파일 읽기
   * 
   * @param file 읽을 excel 파일
   * @param sheetNum 시트 번호
   * @param startRowNum 시작 행 번호
   * @param startCellNum 시작 열 번호
   * @return List<HashMap<Integer, String>>
   * @throws IOException
   */
  public List<HashMap<Integer, String>> readExcelValues(MultipartFile file, int sheetNum,
      int startRowNum, int startCellNum) throws IOException {

    List<HashMap<Integer, String>> resultList = new ArrayList<>();

    // 파일 확장자 취득
    String extension = FileNameUtils.getExtension(file.getOriginalFilename());
    Workbook workbook = null;

    try {
      // 확장자에 따른 Workbook 객체 생성
      if (extension.equals("xlsx"))
        workbook = new XSSFWorkbook(file.getInputStream());
      else if (extension.equals("xls"))
        workbook = new HSSFWorkbook(file.getInputStream());
      else
        throw new IllegalAccessException("[excelUtil.fileExtension] : " + extension);

      // 지정된 시트 취득
      Sheet sheet = workbook.getSheetAt(sheetNum);

      // row 읽기
      for (int r = startRowNum; r < sheet.getPhysicalNumberOfRows(); r++) {
        Row row = sheet.getRow(r);

        if (row != null) {
          HashMap<Integer, String> valuMeMap = readRow(row, startCellNum);
          resultList.add(valuMeMap);
        }
      }

    } catch (Exception e) {
      logger.error("[excelUtil.excelReadSetValue] Exception : " + e.getMessage());
      return new ArrayList<>();
    } finally {
      if (workbook != null)
        workbook.close();
    }

    return resultList;
  }

  /**
   * row 읽기
   * 
   * @param row 행 객체
   * @param startCellNum 시작 열 번호
   * @return HashMap<Integer, String>
   */
  private HashMap<Integer, String> readRow(Row row, int startCellNum) {

    HashMap<Integer, String> valueMap = new HashMap<>();

    for (int c = startCellNum; c < row.getPhysicalNumberOfCells(); c++) {
      Cell cell = row.getCell(c);
      valueMap.put(c, getCellValueAsString(cell));
    }

    return valueMap;
  }

  /**
   * cell 문자열 변환
   * 
   * @param cell 열 객체
   * @return String
   */
  private String getCellValueAsString(Cell cell) {
    if (cell == null)
      return "";

    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue().trim();
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell))
          return dateFormat.format(cell.getDateCellValue());
        else
          return String.valueOf(cell.getNumericCellValue());
      case BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case FORMULA:
        return cell.getCellFormula();
      case BLANK:
        return "";
      default:
        return "";
    }
  }
}
