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

    static final String SECRET_KEY = "12345678901234561234567890123456";

    @PostMapping("/add-or-update-credential")
    public String addOrUpdateCredential(Authentication authentication,
                                        @ModelAttribute("newFile") FileForm newFile,
                                        @ModelAttribute("modifiedNote") Note note,
                                        @ModelAttribute("modifiedCredential") Credential credential,
                                        Model model
    ) {
        Credential[] credentials;
        Credential[] newList = null;
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);

        try {
            Integer credentialId = credential.getCredentialid();

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
            } else {
                credential.setKey(this.SECRET_KEY);
                credentialService.update(credential);
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) message = "Has unexpected error";

            credentials = credentialService.getCredentialsByUser(userId);
            Arrays.stream(credentials).forEach(credentialIt -> {
                credentialIt.setBasepassword(credentialIt.getPassword());
                credentialIt.setPassword(encryptionService.encryptValue(credentialIt.getPassword(), this.SECRET_KEY));
            });

            model.addAttribute("credentials", credentials);
            model.addAttribute("isError", false);
            model.addAttribute("message", message);

            return "home";
        }

        return "redirect:/home";
    }

    @GetMapping("/delete/{credentialid}")
    public String deleteCredential(@PathVariable Integer credential) {
        credentialService.delete(credential);

        return "redirect:/home";
    }
}
