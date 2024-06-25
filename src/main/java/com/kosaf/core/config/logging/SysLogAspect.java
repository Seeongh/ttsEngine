package com.kosaf.core.config.logging;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.kosaf.core.api.log.application.SysLogService;
import com.kosaf.core.api.log.application.dto.syslog.SysLogCreateDTO;
import com.kosaf.core.api.log.value.ErrorStatus;
import com.kosaf.core.api.log.value.ProcessSection;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.ClientInfoUtil;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@Component
@Aspect
@RequiredArgsConstructor
public class SysLogAspect {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private final ObjectMapper objectMapper;

  private final SysLogService sysLogService;

  /**
   * selectLog
   */
  @Pointcut("execution( * com.kosaf.core.api..*Service..find*(..)) "
      + "&& !execution( * com.kosaf.core.api..*Service..findByRoot(..)) "
      + "&& !execution( * com.kosaf.core.api..*Service..findBySubMenu(..)) ")
  private void selectLog() {};

  /**
   * createLog
   */
  @Pointcut("execution( * com.kosaf.core.api..*Service..create*(..)) "
      + "&& !execution( * com.kosaf.core.api..WebLogService.create*(..)) "
      + "&& !execution( * com.kosaf.core.api..SysLogService.create*(..)) "
      + "&& !execution( * com.kosaf.core.api..LoginLogService.create*(..)) ")
  private void createLog() {};

  /**
   * updateLog
   */
  @Pointcut("execution( * com.kosaf.core.api..*Service..update*(..))")
  private void updateLog() {};

  /**
   * deleteLog
   */
  @Pointcut("execution( * com.kosaf.core.api..*Service..delete*(..))")
  private void deleteLog() {};


  /**
   * ※ execution method중 특정 method log 제외
   * : method 상단에 NoLogging annotation을 붙여준다
   */
  @Pointcut("!@annotation(com.kosaf.core.config.logging.NoLogging) ")
  private void noLogging() {};


  @Around("selectLog() && noLogging()")
  public Object selectLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();

    try {
      stopWatch.start();

      Object result = proceedingJoinPoint.proceed();
      return result;

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return e;

    } finally {
      stopWatch.stop();
      //create(proceedingJoinPoint, stopWatch, ProcessSection.R);

    }

  }

  @Around("createLog()")
  public Object createLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    Object result = proceedingJoinPoint.proceed();
    return result;

//    try {
//      stopWatch.start();
//
//      Object result = proceedingJoinPoint.proceed();
//      return result;
//
//    } catch (Exception e) {
//      LOGGER.error(e.getMessage());
//      return null;
//
//    } finally {
//      stopWatch.stop();
//      //create(proceedingJoinPoint, stopWatch, ProcessSection.C);
//
//    }

  }

  @Around("updateLog()")
  public Object updateLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

      Object result = proceedingJoinPoint.proceed();
      return result;
//    try {
//      stopWatch.start();
//
//      Object result = proceedingJoinPoint.proceed();
//      return result;
//
//    } catch (Exception e) {
//      LOGGER.error(e.getMessage());
//      return null;
//
//    } finally {
//      stopWatch.stop();
//      //create(proceedingJoinPoint, stopWatch, ProcessSection.U);
//
//    }

  }

  @Around("deleteLog()")
  public Object deleteLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();

    try {
      stopWatch.start();

      Object result = proceedingJoinPoint.proceed();
      return result;

    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      return null;

    } finally {
      stopWatch.stop();
      create(proceedingJoinPoint, stopWatch, ProcessSection.D);

    }
  }

  /**
   * sysLog 생성
   *
   * @param ProceedingJoinPoint, StopWatch, ProcessSection
   * @throws Throwable
   * @throws IOException
   */
  private void create(ProceedingJoinPoint proceedingJoinPoint, StopWatch stopWatch, ProcessSection processSec) throws Throwable {

    // request
    HttpServletRequest request = ((ServletRequestAttributes)
        RequestContextHolder.currentRequestAttributes()).getRequest();

    // method definition
    MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();

    // method 호출 후 return 값
    Object result = proceedingJoinPoint.proceed();

    ErrorStatus errorYn = ErrorStatus.N;
    int errorStat = 0;

    // ApiResponse 객체일 경우 체크
    if ( result instanceof ApiResponse ) {
      ApiResponse<?> apiResponse = resultToResponse(result);
      LOGGER.info("create syslog > apiResponse.status : {}", apiResponse.getStatus());

      if ( !HttpStatus.valueOf(apiResponse.getStatus()).is2xxSuccessful() ) {
        errorYn = ErrorStatus.Y;
        errorStat = apiResponse.getStatus();
      }
    }

    LOGGER.info("create syslog > uri : {} | processSec : {}", request.getRequestURI(),  processSec);

    if ( !"/api/logout".equals(request.getRequestURI()) ) {

      CustomUserDetails authentication =
              (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

      String svcNm = proceedingJoinPoint.getTarget().getClass().getName();
      String methodNm = signature.getMethod().getName();
      double processTime = stopWatch.getTotalTimeSeconds();
      String rqesterIp = ClientInfoUtil.getIp(request);
      String errorCd = errorStat == 0 ? null : String.valueOf(errorStat);

      SysLogCreateDTO createDTO = SysLogCreateDTO
          .builder()
          .rqesterIp(rqesterIp)
          .rqesterId(authentication.getSysId())
          .trgetMenuNm(null)
          .svcNm(svcNm)
          .methodNm(methodNm)
          .processSeCd(processSec)
          .processTime(processTime)
          .errorYn(errorYn)
          .errorCd(errorCd)
          .build();

      sysLogService.create(createDTO);
    }
  }

  /**
   * resultObject to ApiResponse
   *
   * @param Object
   * @return ApiResponse
   */
  public ApiResponse<?> resultToResponse(Object object) throws IOException {
    StringWriter stringEmp = new StringWriter();

    objectMapper.writeValue(stringEmp, object);

    Map<String, Object> map = objectMapper.readValue(stringEmp.toString(), new TypeReference<Map<String, Object>>() {});
    int status = (int) map.get("status");
    Object data = map.get("data");

    return ApiResponse.of(HttpStatus.valueOf(status), data);

  }

}
