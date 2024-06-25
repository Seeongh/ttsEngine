package com.kosaf.core.web.log.presentation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kosaf.core.api.log.application.dto.weblog.WebLogDetailDTO;

@Controller
@RequestMapping("/log/webLog")
public class WebLogController {

  /**
   * 웹 로그 목록
   * @param Model
   * @return String page
   */
  @RequestMapping("/list")
  public String webLogList(Model model) {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date now = new Date();

    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -1);

    String start = sdf.format(cal.getTime());
    String end = sdf.format(now);

    model.addAttribute("startLogDt", start);
    model.addAttribute("endLogDt", end);

    return "log/webLog/webLogList";
  }

  /**
   * 웹 로그 상세
   * @param WebLogDetailDTO, Model
   * @return String page
   */
  @RequestMapping("/info")
  public String webLogInfo(WebLogDetailDTO detailDTO, Model model) {

    model.addAttribute("detailDTO", detailDTO);

    return "log/webLog/webLogInfo";
  }
}
