package com.tsimura.service.impl;

import com.tsimura.domain.Role;
import com.tsimura.domain.security.CurrentUser;
import com.tsimura.service.CurrentUserService;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService  {
    @Override
    public boolean canAccessUser(CurrentUser currentUser, Long userId) {
        return currentUser != null
                && (currentUser.getRole().equals(Role.ADMIN) || currentUser.getId().equals(userId));
    }
}
