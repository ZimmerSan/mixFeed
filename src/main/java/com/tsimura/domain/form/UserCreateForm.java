package com.tsimura.domain.form;

import com.tsimura.domain.Role;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class UserCreateForm {

    @NotEmpty
    private String username = "";

    @NotEmpty
    private String password = "";

    @NotEmpty
    private String passwordConfirm = "";

    @NotNull
    private Role role = Role.USER;

}
