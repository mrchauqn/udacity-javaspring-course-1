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
                                  Model model
    ) {
        Note[] notes;
        String username = authentication.getName();
        Integer userId = userService.getUserId(username);

        try {
            Integer noteId = note.getNoteid();

            if (noteId == null) {
                noteService.insert(new Note(null, note.getNotetitle(), note.getNotedescription(), userId));
            } else {
                noteService.update(note);
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null) message = "Has unexpected error";

            notes = noteService.getNotesByUser(userId);

            model.addAttribute("notes", notes);
            model.addAttribute("isError", false);
            model.addAttribute("message", message);

            return "home";
        }

        return "redirect:/home";
    }

    @GetMapping("/delete/{noteid}")
    public String deleteNote(@PathVariable Integer noteid) {
        noteService.delete(noteid);

        return "redirect:/home";
    }
}
