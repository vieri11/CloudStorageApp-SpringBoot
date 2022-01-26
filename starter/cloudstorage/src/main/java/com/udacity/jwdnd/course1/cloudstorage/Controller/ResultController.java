package com.udacity.jwdnd.course1.cloudstorage.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

public class ResultController {
    @RequestMapping("/result")
    //map object SPRING provides to methods called to handle requests
    public String getHomePage(Model model) {
        //add attributed to Model in name-value pairs and make them available to HTML template chosen in method
        model.addAttribute("greetings", new String[] {"hello", "hi", "goodbye"});
        return "result";
    }
}
