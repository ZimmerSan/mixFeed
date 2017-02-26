package com.tsimura.service;

import com.tsimura.domain.social.SocialAccount;

import java.util.Optional;

public interface SocialAccountService <T extends SocialAccount> {
    T save (T account);

    Optional<T> findById(String id);

    Iterable<T> findByOwner_Id(Long id);

    Iterable<T> findAll();
}
