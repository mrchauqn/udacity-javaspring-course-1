package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @GetMapping
    public String getHome(Authentication authentication, @ModelAttribute("newFile") FileForm newFile, Model model) {
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);

        model.addAttribute("files", fileService.getListFilesByUser(userId));

        return "home";
    }

    @GetMapping(value = "/get-file/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getFile(@PathVariable String filename) {
        return fileService.getFile(filename).getFiledata();
    }

    @PostMapping
    public String newFile(Authentication authentication, @ModelAttribute("newFile") FileForm newFile, Model model) {
        boolean isError = false;
        String message;
        String[] files;
        String[] newList = null;

        try {
            String username = authentication.getName();
            Integer userId = userService.getUserId(username);
            MultipartFile newFileObj = newFile.getFile();
            String newFileName = newFileObj.getOriginalFilename();

            files = fileService.getListFilesByUser(userId);
            newList = files;

            if (StringUtils.isEmpty(newFileName)) {
                throw new Exception("Please choose a file to upload!");
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

            if (rows < 1){
                throw new Exception();
            } else {
                message = "Upload file successfully!";
                newList = Arrays.copyOf(files, files.length + 1);
                newList[files.length] = newFileName;
            }

        } catch (Exception e) {
            isError = true;
            message = e.getMessage();
            if (message == null) message = "Has unexpected error";
        }

        model.addAttribute("files", newList);
        model.addAttribute("isError", isError);
        model.addAttribute("message", message);

        return "home";
    }


}
