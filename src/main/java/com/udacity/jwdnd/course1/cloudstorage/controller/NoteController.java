package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("note")
@Controller
public class NoteController {

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;

    @PostMapping("/add-or-update-note")
    public String addOrUpdateNote(Authentication authentication,
                                  @ModelAttribute("newFile") FileForm newFile,
                                  @ModelAttribute("modifiedNote") Note note,
                                  @ModelAttribute("modifiedCredential") Credential credential,
                                  RedirectAttributes redirectAttributes
    ) {
        String message;
        boolean isError = false;

        try {
            Integer noteId = note.getNoteid();
            String username = authentication.getName();
            Integer userId = userService.getUserId(username);

            if (noteId == null) {
                noteService.insert(new Note(null, note.getNotetitle(), note.getNotedescription(), userId));
                message = "Add note successfully!";
            } else {
                noteService.update(note);
                message = "Update note successfully!";
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

    @GetMapping("/delete/{noteid}")
    public String deleteNote(@PathVariable Integer noteid, RedirectAttributes redirectAttributes) {
        noteService.delete(noteid);
        redirectAttributes.addFlashAttribute("isError", false);
        redirectAttributes.addFlashAttribute("message", "Delete note successfully!");

        return "redirect:/home";
    }
}
