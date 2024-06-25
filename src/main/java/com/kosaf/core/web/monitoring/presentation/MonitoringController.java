package com.kosaf.core.web.monitoring.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {


    @RequestMapping("/ttsServer")
    public String replaceKeyword(){
        return "/monitoring/ttsServer";
    }
}
