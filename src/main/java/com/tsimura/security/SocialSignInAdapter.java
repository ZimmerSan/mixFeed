package com.tsimura.security;

import com.tsimura.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

@Slf4j
@Service
public class SocialSignInAdapter implements SignInAdapter {

    private final SecurityService securityService;

    @Autowired
    public SocialSignInAdapter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public String signIn(String id, Connection<?> connection, NativeWebRequest request) {
        log.debug("SocialSignInAdapter.signIn invoked. id = {}", id);

        securityService.autoLogin(Long.valueOf(id));
        return null;
    }

}
