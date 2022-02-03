package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private FileService fileService;
    private EncryptionService encryptionService;
    private GetUserIdService getUserIdService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, FileService fileService,
                          EncryptionService encryptionService, GetUserIdService getUserIdService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.getUserIdService = getUserIdService;
    }

    @GetMapping
    //map object SPRING provides to methods called to handle requests
    public String getHomePage(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        model.addAttribute("noteList", noteService.getNoteListByUserId(getUserIdService.getUserId(authentication)));
        model.addAttribute("credentialList", credentialService.getCredentialListByUserId(getUserIdService.getUserId(authentication)));
        model.addAttribute("fileList", fileService.getFileListByUserId(getUserIdService.getUserId(authentication)));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }
}
