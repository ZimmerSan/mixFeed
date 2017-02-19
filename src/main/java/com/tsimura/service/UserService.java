package com.tsimura.service;

import com.tsimura.domain.User;
import com.tsimura.domain.form.UserCreateForm;

import java.util.Optional;

public interface UserService {
    User create(UserCreateForm user);

    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);

    Iterable<User> findAll();
}
