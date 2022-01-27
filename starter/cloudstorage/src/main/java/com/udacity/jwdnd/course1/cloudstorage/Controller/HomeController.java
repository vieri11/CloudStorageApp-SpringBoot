package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;

    public HomeController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    //map object SPRING provides to methods called to handle requests
    public String getHomePage(Authentication authentication, NoteForm noteForm, Model model) {
        model.addAttribute("noteList", noteService.getNoteListByUserId(getUserId(authentication)));
        return "home";
    }

    @PostMapping("/addNote")
    //map object SPRING provides to methods called to handle requests
    public String addNote(Authentication authentication, NoteForm noteForm, Model model) {
        noteForm.setUserId(getUserId(authentication));
        this.noteService.addNote(noteForm);

        model.addAttribute("result", "success");
        return "result";
    }

    private Integer getUserId(Authentication authentication) {
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);

        return userId;
    }
}
