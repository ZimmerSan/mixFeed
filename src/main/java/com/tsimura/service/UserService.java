package com.tsimura.service;

import com.tsimura.domain.User;
import com.tsimura.domain.form.UserCreateForm;

public interface UserService {
    User create(UserCreateForm user);

    User findById(long id);

    User findByUsername(String username);

    Iterable<User> findAll();
}
