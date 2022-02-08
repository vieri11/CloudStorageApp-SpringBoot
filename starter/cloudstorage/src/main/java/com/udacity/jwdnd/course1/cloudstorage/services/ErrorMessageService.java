package com.udacity.jwdnd.course1.cloudstorage.services;

import org.springframework.stereotype.Service;

@Service
public class ErrorMessageService {

    public static final String successMessage = "success";
    public static final String duplicateFilenameMessage = "Duplicate File Name!";
    public static final String noFileMessage = "No File Name!";
    public static final String emptyFileMessage = "Empty File!";
    public static final String fileTooLargeMessage = "File too large!";
    public static final String duplicateUsernameMessage = "The username already exists.";
    public static final String signupErrorMessage = "There was an error signing you up. Please try again.";
}
