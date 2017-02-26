package com.tsimura.security;

import com.tsimura.domain.User;
import com.tsimura.domain.form.UserCreateForm;
import com.tsimura.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Slf4j
public class SocialConnectionSignUp implements ConnectionSignUp {

    private final UserService userService;

    public SocialConnectionSignUp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(Connection<?> connection) {
        log.debug("SocialConnectionSignUp.execute invoked.");

        UserCreateForm form = new UserCreateForm();
        form.setUsername(generateUsername(connection));
        form.setPassword(randomAlphabetic(8));

        User user = userService.create(form);
        log.debug("user = {}", user);
        return user.getId().toString();
    }

    private String generateUsername(Connection<?> connection){
        ConnectionKey key = connection.getKey();
        return key.getProviderId() + "_" + key.getProviderUserId();
    }

}
