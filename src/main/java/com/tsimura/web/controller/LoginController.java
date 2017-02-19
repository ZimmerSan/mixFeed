package com.tsimura.web.controller;

import com.tsimura.domain.form.UserCreateForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
public class LoginController {

    @GetMapping(value = "/registration")
    public String getRegistrationPage(Model model, @RequestParam Optional<String> error) {
        model.addAttribute("userForm", new UserCreateForm());
        model.addAttribute("error", error);

        return "auth/registration";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(Model model, @RequestParam Optional<String> error) {
        model.addAttribute("error", error);

        return "auth/login";
    }

}
