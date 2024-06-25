package com.kosaf.core.web.log.presentation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kosaf.core.api.log.application.dto.syslog.SysLogDetailDTO;

@Controller
@RequestMapping("/log/sysLog")
public class SysLogContoller {

  /**
   * 시스템 로그 목록
   * @param Model
   * @return String page
   */
  @RequestMapping("/list")
  public String sysLogList(Model model) {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date now = new Date();

    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -1);

    String start = sdf.format(cal.getTime());
    String end = sdf.format(now);

    model.addAttribute("startLogDt", start);
    model.addAttribute("endLogDt", end);

    return "log/sysLog/sysLogList";
  }

  /**
   * 시스템 로그 상세
   * @param SysLogDetailDTO, Model
   * @return String page
   */
  @RequestMapping("/info")
  public String sysLogInfo(SysLogDetailDTO detailDTO, Model model) {

    model.addAttribute("detailDTO", detailDTO);

    return "log/sysLog/sysLogInfo";
  }
}
