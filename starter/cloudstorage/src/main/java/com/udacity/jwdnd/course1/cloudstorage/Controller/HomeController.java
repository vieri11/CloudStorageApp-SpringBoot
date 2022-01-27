package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @GetMapping
    //map object SPRING provides to methods called to handle requests
    public String getHomePage(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        model.addAttribute("noteList", noteService.getNoteListByUserId(getUserId(authentication)));
        model.addAttribute("credentialList", credentialService.getCredentialListByUserId(getUserId(authentication)));
        return "home";
    }

    @PostMapping("/addNote")
    //map object SPRING provides to methods called to handle requests
    public String addNote(Authentication authentication, NoteForm noteForm, Model model) {

        noteForm.setUserId(getUserId(authentication));

        if(noteForm.getNoteId() == null)
            this.noteService.addNote(noteForm);
        else
            this.noteService.editNote(noteForm);

        model.addAttribute("result", "success");
        return "result";
    }

    @GetMapping("/deleteNote/{noteId}")
    public String deleteNote (@PathVariable Integer noteId, Model model) {
        noteService.deleteNote(noteId);

        model.addAttribute("result", "success");
        return "result";
    }

    @PostMapping("/addCredential")
    public String addCredential(Authentication authentication, CredentialForm credentialForm, Model model) {
        credentialForm.setUserId(getUserId(authentication));

        this.credentialService.addCredential(credentialForm);

        model.addAttribute("result", "success");
        return "result";
    }

    @GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredential (@PathVariable Integer credentialId, Model model) {
        credentialService.deleteCredential(credentialId);

        model.addAttribute("result", "success");
        return "result";
    }

    private Integer getUserId(Authentication authentication) {
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);

        return userId;
    }
}
