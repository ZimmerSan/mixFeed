package com.tsimura.web.controller;

import com.tsimura.domain.form.UserCreateForm;
import com.tsimura.service.SecurityService;
import com.tsimura.service.UserService;
import com.tsimura.validator.UserCreateFormValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final UserCreateFormValidator userCreateFormValidator;

    @Autowired
    public UserController(UserCreateFormValidator userCreateFormValidator, UserService userService, SecurityService securityService) {
        this.userCreateFormValidator = userCreateFormValidator;
        this.userService = userService;
        this.securityService = securityService;
    }

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @GetMapping("/users/")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.findAll());

        return "users/all_users";
    }

    //    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/")
    public String createUser(@Valid @ModelAttribute("userForm") UserCreateForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "auth/registration";

        try {
            log.debug("Validation correct. Form = {}", form);
            userService.create(form);
            securityService.autologin(form.getUsername(), form.getPasswordConfirm());
        } catch (DataIntegrityViolationException e) {
            log.error("createUser error:", e);
            bindingResult.reject("email.exists", "Duplicate.userForm.username");
            return "auth/registration";
        }

        return "redirect:/";
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
    @GetMapping("/users/{id}")
    public String getUserPage(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));

        return "users/user";
    }

}