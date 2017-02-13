package com.tsimura.service.impl;

import com.tsimura.domain.User;
import com.tsimura.domain.form.UserCreateForm;
import com.tsimura.repository.UserRepository;
import com.tsimura.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder crypt;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder crypt) {
        this.userRepository = userRepository;
        this.crypt = crypt;
    }

    @Override
    public User create(UserCreateForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(crypt.encode(form.getPassword()));
        user.setRole(form.getRole());
        return userRepository.save(user);
    }

    @Override
    public User findById(long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll(new Sort("username"));
    }
}