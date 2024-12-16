package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    UserService userService;

    @GetMapping
    public String signupView() {
        return "signup";
    }

    @PostMapping
    public String postUser(User user, RedirectAttributes redirectAttributes) {
        boolean isError = false;
        String message = null;

        try {
            String userName = user.getUsername();

            if (userService.isExistUsername(userName)) {
                isError = true;
                message = "Already exist this user name!";
            } else {
                Integer addedNum = userService.createUser(user);
                if (addedNum < 1) {
                    isError = true;
                    message = "An unexpected error happens!";
                } else {
                    message = "You successfully signed up!";
                }
            }
        } catch (Exception e) {
            message = e.getMessage();
        }

        redirectAttributes.addFlashAttribute("isError", isError);
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/login";
    }
}
