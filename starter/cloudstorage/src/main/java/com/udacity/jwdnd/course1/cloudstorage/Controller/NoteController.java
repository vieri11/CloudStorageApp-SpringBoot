package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.ErrorMessageService;
import com.udacity.jwdnd.course1.cloudstorage.services.GetUserIdService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class NoteController {
    private NoteService noteService;
    private GetUserIdService getUserIdService;
    private ErrorMessageService errorMessageService;

    public NoteController(NoteService noteService, GetUserIdService getUserIdService, ErrorMessageService errorMessageService) {
        this.noteService = noteService;
        this.getUserIdService = getUserIdService;
        this.errorMessageService = errorMessageService;
    }

    @PostMapping("/addNote")
    //map object SPRING provides to methods called to handle requests
    public String addNote(Authentication authentication, NoteForm noteForm, Model model) {
        noteForm.setUserId(getUserIdService.getUserId(authentication));

        if(noteForm.getNoteId() == null)
            this.noteService.addNote(noteForm);
        else
            this.noteService.editNote(noteForm);

        model.addAttribute("result", errorMessageService.successMessage);
        return "result";
    }

    @GetMapping("/deleteNote/{noteId}")
    public String deleteNote (@PathVariable Integer noteId, Model model) {
        noteService.deleteNote(noteId);

        model.addAttribute("result", errorMessageService.successMessage);
        return "result";
    }
}
