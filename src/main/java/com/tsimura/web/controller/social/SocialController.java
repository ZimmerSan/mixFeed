package com.tsimura.web.controller.social;

import com.tsimura.domain.security.CurrentUser;
import com.tsimura.domain.social.FacebookAccount;
import com.tsimura.service.SecurityService;
import com.tsimura.service.SocialAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/social")
public class SocialController {

    @Qualifier("facebookAccountService")
    private final SocialAccountService<FacebookAccount> facebookAccountService;
    private final UsersConnectionRepository connectionRepository;
    private final SecurityService securityService;
    private final Facebook facebook;

    @Autowired
    public SocialController(SocialAccountService<FacebookAccount> facebookAccountService,
                            UsersConnectionRepository connectionRepository,
                            SecurityService securityService,
                            Facebook facebook) {
        this.facebookAccountService = facebookAccountService;
        this.connectionRepository = connectionRepository;
        this.securityService = securityService;
        this.facebook = facebook;
    }

    @GetMapping("")
    public String connectFacebook(Model model, @RequestParam Optional<String> provider, @RequestParam Optional<String> status){
        if (provider.isPresent() && status.get().equalsIgnoreCase("ok"))
            model.addAttribute("success", provider.get());

        CurrentUser user = securityService.getAuthenticatedUser();

        ConnectionRepository repository = this.connectionRepository.createConnectionRepository(user.getId().toString());
        MultiValueMap<String, Connection<?>> allConnections = repository.findAllConnections();
        log.debug("allConnections = {}", allConnections);

        Iterable<FacebookAccount> facebookAccounts = facebookAccountService.findByOwner_Id(user.getId());
        log.debug("facebookAccounts = {}", facebookAccounts);

        return "social/social";
    }

}
