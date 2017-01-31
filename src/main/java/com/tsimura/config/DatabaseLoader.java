package com.tsimura.config;

import com.tsimura.domain.User;
import com.tsimura.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseLoader.class);


    private final UserRepository repository;

    @Autowired
    public DatabaseLoader(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
        repository.save(new User("paul"));
        repository.save(new User("bob"));

        LOGGER.debug("Users: {}", repository.findAll());
    }
}
