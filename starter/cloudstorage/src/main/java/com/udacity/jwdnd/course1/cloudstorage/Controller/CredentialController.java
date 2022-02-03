package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.ErrorMessageService;
import com.udacity.jwdnd.course1.cloudstorage.services.GetUserIdService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class CredentialController {
    private CredentialService credentialService;
    private GetUserIdService getUserIdService;
    private ErrorMessageService errorMessageService;

    public CredentialController(CredentialService credentialService, GetUserIdService getUserIdService, ErrorMessageService errorMessageService) {
        this.credentialService = credentialService;
        this.getUserIdService = getUserIdService;
        this.errorMessageService = errorMessageService;
    }

    @PostMapping("/addCredential")
    public String addCredential(Authentication authentication, CredentialForm credentialForm, Model model) {
        credentialForm.setUserId(getUserIdService.getUserId(authentication));

        if(credentialForm.getCredentialId() == null)
            this.credentialService.addCredential(credentialForm);
        else
            this.credentialService.editCredential(credentialForm);

        model.addAttribute("result", errorMessageService.successMessage);
        return "result";
    }

    @GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredential (@PathVariable Integer credentialId, Model model) {
        credentialService.deleteCredential(credentialId);

        model.addAttribute("result", errorMessageService.successMessage);
        return "result";
    }
}
