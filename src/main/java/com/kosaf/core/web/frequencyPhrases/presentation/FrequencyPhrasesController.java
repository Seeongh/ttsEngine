package com.kosaf.core.web.frequencyPhrases.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frequency")
public class FrequencyPhrasesController {

    @RequestMapping("/phrasesList")
    public String replaceKeyword(){
        return "/frequencyPhrases/phrasesManage";
    }
}
