package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@RequestMapping("credential")
@Controller
public class CredentialController {
    @Autowired
    private UserService userService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private EncryptionService encryptionService;

    public static final String SECRET_KEY = "12345678901234561234567890123456";

    @PostMapping("/add-or-update-credential")
    public String addOrUpdateCredential(Authentication authentication,
                                        @ModelAttribute("newFile") FileForm newFile,
                                        @ModelAttribute("modifiedNote") Note note,
                                        @ModelAttribute("modifiedCredential") Credential credential,
                                        RedirectAttributes redirectAttributes
    ) {
        String message;
        boolean isError = false;

        try {
            Integer credentialId = credential.getCredentialid();
            String username = authentication.getName();
            Integer userId = userService.getUserId(username);

            if (credentialId == null) {
                credentialService.insert(
                        new Credential(
                                null,
                                credential.getUrl(),
                                credential.getUsername(),
                                this.SECRET_KEY,
                                credential.getPassword(),
                                userId
                        ));
                message = "Create credential successfully!";
            } else {
                credential.setKey(this.SECRET_KEY);
                credentialService.update(credential);
                message = "Update credential successfully!";
            }
        } catch (Exception e) {
            isError = true;
            message = e.getMessage();
            if (message == null) message = "Has unexpected error";
        }

        redirectAttributes.addFlashAttribute("isError", isError);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/home";
    }

    @GetMapping("/delete/{credentialid}")
    public String deleteCredential(@PathVariable Integer credentialid, RedirectAttributes redirectAttributes) {
        credentialService.delete(credentialid);
        redirectAttributes.addFlashAttribute("isError", false);
        redirectAttributes.addFlashAttribute("message", "Delete credential successfully!");

        return "redirect:/home";
    }
}
