package com.kosaf.core.web.replaceKeyword.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/replace")
public class ReplaceKeywordController {

    @RequestMapping("/keywordList")
    public String replaceKeyword(){
        return "/replaceKeyword/replaceManage";
    }
}
