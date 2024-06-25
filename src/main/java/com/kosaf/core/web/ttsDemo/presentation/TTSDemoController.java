package com.kosaf.core.web.ttsDemo.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/ttsDemo")
public class TTSDemoController {

    @GetMapping("/play")
    public String ttsDemo() {
        return "/ttsDemo/playTTS";
    }

}
