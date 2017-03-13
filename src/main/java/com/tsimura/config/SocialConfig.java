package com.tsimura.config;

import com.tsimura.security.SocialConnectionSignUp;
import com.tsimura.security.SocialSignInAdapter;
import com.tsimura.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.social.vkontakte.connect.VKontakteConnectionFactory;

import javax.sql.DataSource;

@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    //
    // SocialConfigurer implementation methods
    //

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment env) {
        configurer.addConnectionFactory(new VKontakteConnectionFactory(
                env.getProperty("spring.social.vk.appId"),
                env.getProperty("spring.social.vk.appSecret")));
    }

    @Override
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());

        repository.setConnectionSignUp(new SocialConnectionSignUp(userService));
        return repository;
    }

    //
    // API Binding Beans
    //

    @Bean
    @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
    public VKontakte vkontakte(ConnectionRepository repository) {
        Connection<VKontakte> connection = repository.findPrimaryConnection(VKontakte.class);
        return connection != null ? connection.getApi() : null;
    }

    //
    // Web Controller and Filter Beans
    //

    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository);
        // TODO: 06.03.2017
//        connectController.addInterceptor(new FacebookConnectionInterceptor());
        return connectController;
    }

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository, SocialSignInAdapter socialSignInAdapter) {
        return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, socialSignInAdapter);
    }

}
