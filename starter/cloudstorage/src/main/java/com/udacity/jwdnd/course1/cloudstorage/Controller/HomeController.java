package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private FileService fileService;

    public HomeController(NoteService noteService, UserService userService, CredentialService credentialService, FileService fileService) {
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
        this.fileService = fileService;
    }

    @GetMapping
    //map object SPRING provides to methods called to handle requests
    public String getHomePage(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model) {
        model.addAttribute("noteList", noteService.getNoteListByUserId(getUserId(authentication)));
        model.addAttribute("credentialList", credentialService.getCredentialListByUserId(getUserId(authentication)));
        model.addAttribute("fileList", fileService.getFileListByUserId(getUserId(authentication)));
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

        if(credentialForm.getCredentialId() == null)
            this.credentialService.addCredential(credentialForm);
        else
            this.credentialService.editCredential(credentialForm);

        model.addAttribute("result", "success");
        return "result";
    }

    @GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredential (@PathVariable Integer credentialId, Model model) {
        credentialService.deleteCredential(credentialId);

        model.addAttribute("result", "success");
        return "result";
    }

    @PostMapping("/fileUpload")
    public String addFile(Authentication authentication, @RequestParam("fileUpload")MultipartFile fileUpload, Model model) throws IOException {
        Integer userId = getUserId(authentication);
        String originalFileName = fileUpload.getOriginalFilename();
        Boolean isDup = fileService.isDup(originalFileName, userId);

        // ERROR handling
        if(isDup) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "Duplicate File Name!");
            return "result";
        }

        if(originalFileName.isEmpty()) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "Empty File Name!");
            return "result";
        }

        if(fileUpload.getSize() == 0) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", "Empty File!");
            return "result";
        }

        // handle IO Exception
        try {
            InputStream fis = fileUpload.getInputStream();

            // read in file data
            byte[] fileData = new byte[(int)fileUpload.getSize()];
            fis.read(fileData);

            File newFile = new File(originalFileName, fileUpload.getContentType(), ""+fileUpload.getSize(),
                    userId, fileData);

            this.fileService.addFile(newFile);
        }
        catch (Exception e) {
            throw new IOException("Error uploading file: " + originalFileName + " Please try again.");
        }

        model.addAttribute("result", "success");
        return "result";
    }

    @GetMapping(
            value = "/viewFile/{fileId}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> viewFile (@PathVariable Integer fileId, Model model) {

        File file = fileService.viewFile(fileId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "inline;filename=" + file.getFileName());
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
                file.getFileData(), headers, HttpStatus.OK);

        return response;
    }

    @GetMapping("/deleteFile/{fileId}")
    public String deleteFile (@PathVariable Integer fileId, Model model) {
        fileService.deleteFile(fileId);

        model.addAttribute("result", "success");
        return "result";
    }

    private Integer getUserId(Authentication authentication) {
        String username = authentication.getName();
        Integer userId = this.userService.getUserId(username);

        return userId;
    }
}
