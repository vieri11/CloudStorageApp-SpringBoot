package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.ErrorMessageService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.GetUserIdService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/home")
public class FileController {
    private FileService fileService;
    private GetUserIdService getUserIdService;
    private ErrorMessageService errorMessageService;

    public FileController(FileService fileService, GetUserIdService getUserIdService, ErrorMessageService errorMessageService) {
        this.fileService = fileService;
        this.getUserIdService = getUserIdService;
        this.errorMessageService = errorMessageService;
    }

    @PostMapping("/fileUpload")
    public String addFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) throws IOException {
        Integer userId = getUserIdService.getUserId(authentication);
        String originalFileName = fileUpload.getOriginalFilename();
        Boolean isDup = fileService.isDup(originalFileName, userId);

        // ERROR handling
        if(isDup) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", errorMessageService.duplicateFilenameMessage);
            return "result";
        }

        if(originalFileName.isEmpty()) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", errorMessageService.noFileMessage);
            return "result";
        }

        if(fileUpload.getSize() == 0) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", errorMessageService.emptyFileMessage);
            return "result";
        }

        if(fileUpload.getSize() > 10000000) {
            model.addAttribute("result", "error");
            model.addAttribute("errorMessage", errorMessageService.fileTooLargeMessage);
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

        model.addAttribute("result", errorMessageService.successMessage);
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

        model.addAttribute("result", errorMessageService.successMessage);
        return "result";
    }
}
