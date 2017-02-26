package com.tsimura.service.impl;

import com.tsimura.domain.social.FacebookAccount;
import com.tsimura.repository.FacebookAccountRepository;
import com.tsimura.service.SocialAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("facebookAccountService")
public class FacebookAccountServiceImpl implements SocialAccountService <FacebookAccount> {


    private final FacebookAccountRepository repository;

    @Autowired
    public FacebookAccountServiceImpl(FacebookAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public FacebookAccount save(FacebookAccount account) {
        return repository.save(account);
    }

    @Override
    public Optional<FacebookAccount> findById(String id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    @Override
    public Iterable<FacebookAccount> findByOwner_Id(Long id) {
        return repository.findByOwner_Id(id);
    }

    @Override
    public Iterable<FacebookAccount> findAll() {
        return repository.findAll();
    }

}
