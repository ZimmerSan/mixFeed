package com.tsimura.web.controller;

import com.tsimura.domain.form.UserCreateForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping(value = "/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("form", new UserCreateForm());

        return "auth/registration";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(Model model, @RequestParam String error, @RequestParam String logout) {
        if (error != null) model.addAttribute("error", "The email or password you have entered is invalid.");
        if (logout != null) model.addAttribute("message", "You have been logged out successfully.");

        return "auth/login";
    }

}
