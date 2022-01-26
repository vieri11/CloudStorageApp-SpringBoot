package com.udacity.jwdnd.course1.cloudstorage.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

@Controller
public class HomeController {

    @RequestMapping("/home")
    //map object SPRING provides to methods called to handle requests
    public String getHomePage(Model model) {
        //add attributed to Model in name-value pairs and make them available to HTML template chosen in method
        model.addAttribute("greetings", new String[] {"hello", "hi", "goodbye"});
        return "home";
    }
}
