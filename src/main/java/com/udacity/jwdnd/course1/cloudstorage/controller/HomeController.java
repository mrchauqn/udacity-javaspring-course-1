package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

import static com.udacity.jwdnd.course1.cloudstorage.controller.CredentialController.SECRET_KEY;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private EncryptionService encryptionService;

    @GetMapping
    public String getHome(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("modifiedNote") Note modifiedNote,
            @ModelAttribute("modifiedCredential") Credential modifiedCredential,
            Model model) {
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);

        model.addAttribute("files", fileService.getListFilesByUser(userId));
        model.addAttribute("notes", noteService.getNotesByUser(userId));

        Credential[] credentials = credentialService.getCredentialsByUser(userId);
        Arrays.stream(credentials).forEach(credentialIt -> {
            credentialIt.setBasepassword(credentialIt.getPassword());
            credentialIt.setPassword(encryptionService.encryptValue(credentialIt.getPassword(), SECRET_KEY));
        });
        model.addAttribute("credentials", credentials);

        return "home";
    }


    // =========== FILES ===========

    @PostMapping
    public String newFile(
            Authentication authentication,
            @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("modifiedNote") Note modifiedNote,
            @ModelAttribute("modifiedCredential") Credential modifiedCredential,
            RedirectAttributes redirectAttributes
    ) {
        boolean isError = false;
        String message;


        try {
            String username = authentication.getName();
            Integer userId = userService.getUserId(username);

            MultipartFile newFileObj = newFile.getFile();
            String newFileName = newFileObj.getOriginalFilename();

            if (StringUtils.isEmpty(newFileName)) {
                throw new Exception("Please choose a file to upload!");
            }

            if (newFileObj.getSize() >= (5 * 1024 * 1024)) {
                throw new Exception("Only upload file that its size less than 5MB!");
            }

            String[] uploadedFiles = fileService.getListFilesByUser(userId);
            for (String file : uploadedFiles) {
                if (file.equals(newFileName)) {
                    throw new Exception("File " + newFileName + " is same name with existed one");
                }
            }

            File fileWillSave = new File(null, newFileName, newFileObj.getContentType(),
                    String.valueOf(newFileObj.getSize()), userId, newFileObj.getBytes());

            int rows = fileService.insert(fileWillSave);

            if (rows < 1) {
                throw new Exception();
            } else {
                message = "Upload file successfully!";
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

    @GetMapping(value = "/get-file/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getFile(@PathVariable String filename) {
        return fileService.getFile(filename).getFiledata();
    }


    @GetMapping(value = "/delete-file/{filename}")
    public String delete(Authentication authentication,
                         @ModelAttribute("newFile") FileForm newFile,
                         @PathVariable String filename, Model model,
                         RedirectAttributes redirectAttributes
    ) {
        boolean isError = false;
        String message;

        try {

            String username = authentication.getName();
            Integer userId = userService.getUserId(username);

            fileService.deleteFileByUser(filename, userId);
            message = "Delete file success!";
        } catch (Exception e) {
            isError = true;
            message = e.getMessage();
            if (message == null) message = "Has unexpected error";
        }

        redirectAttributes.addFlashAttribute("isError", isError);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/home";
    }
}
