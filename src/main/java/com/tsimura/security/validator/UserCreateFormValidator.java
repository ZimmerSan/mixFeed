package com.tsimura.security.validator;

import com.tsimura.domain.form.UserCreateForm;
import com.tsimura.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserCreateFormValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserCreateFormValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserCreateForm form = (UserCreateForm) o;

        validatePasswords(errors, form);
        validateUsername(errors, form);
    }

    private void validatePasswords(Errors errors, UserCreateForm form) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (form.getPassword().length() < 8 || form.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }
        if (!form.getPassword().equals(form.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }

    private void validateUsername(Errors errors, UserCreateForm form) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (form.getUsername().length() < 6 || form.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (userService.findByUsername(form.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }
    }
}
