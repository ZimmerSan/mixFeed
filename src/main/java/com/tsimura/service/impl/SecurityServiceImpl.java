package com.tsimura.service.impl;

import com.tsimura.domain.User;
import com.tsimura.domain.security.CurrentUser;
import com.tsimura.service.SecurityService;
import com.tsimura.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Autowired
    public SecurityServiceImpl(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails instanceof UserDetails)
            return ((UserDetails) userDetails).getUsername();

        return null;
    }

    @Override
    public CurrentUser getAuthenticatedUser() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(findLoggedInUsername());
        if (userDetails instanceof CurrentUser)
            return (CurrentUser) userDetails;

        return null;
    }

    @Override
    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(token);

        if (token.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(token);
            log.debug("Auto login {} successfully!", username);
        }
    }

    @Override
    public void autoLogin(Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id=%s was not found", id)));

        autoLogin(user.getUsername(), user.getPassword());
    }
}
