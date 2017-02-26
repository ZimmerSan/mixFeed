package com.tsimura.service;

import com.tsimura.domain.security.CurrentUser;

public interface SecurityService {
    String findLoggedInUsername();
    CurrentUser getAuthenticatedUser();

    void autoLogin(String username, String password);
    void autoLogin(Long id);
}
