package com.udacity.jwdnd.course1.cloudstorage.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/result")
public class ResultController {

    @GetMapping
    public String resultPage(Model model) {
        return "result";
    }
}
