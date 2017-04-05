package com.tsimura.web.controller;

import com.tsimura.domain.Photo;
import com.tsimura.domain.security.CurrentUser;
import com.tsimura.service.PhotoService;
import com.tsimura.service.SecurityService;
import com.tsimura.web.VkHelper;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/")
public class MainController {

    private final UsersConnectionRepository connectionRepository;
    private final PhotoService photoService;
    private final SecurityService securityService;

    @Autowired
    public MainController(UsersConnectionRepository connectionRepository, PhotoService photoService, SecurityService securityService) {
        this.connectionRepository = connectionRepository;
        this.photoService = photoService;
        this.securityService = securityService;
    }

    @ModelAttribute("source")
    public String source() {
        return "vk";
    }

    @GetMapping("")
    public String getProfile(ModelMap model) {
        CurrentUser authenticatedUser = securityService.getAuthenticatedUser();
        ConnectionRepository repository = this.connectionRepository.createConnectionRepository(authenticatedUser.getId().toString());
        Connection<VKontakte> vkConnection = repository.findPrimaryConnection(VKontakte.class);
        UserActor userActor = vkConnection.getApi().getUserActor();

        return "redirect:/friends" + "?user_id=" + userActor.getId();
    }

    @GetMapping("/photos")
    public String findUserPhotos(Model model, @RequestParam(name = "user_id") Optional<String> userIdParam) {
        if (userIdParam.isPresent()) {
            try {
                new URL(userIdParam.get());
            } catch (MalformedURLException e) {
                // Not an URL
            }
            Integer userId = Integer.valueOf(userIdParam.get());
            UserXtrCounters user = VkHelper.parseUser(userId);
            model.addAttribute("user", user);

            List<Photo> photos = photoService.findByUserId(userId);
            model.addAttribute("photos", photos);

            int photoCount = photoService.countByUserId(userId);
            model.addAttribute("photoCount", photoCount);

            int groupsCount = photoService.groupsCountByUserId(userId);
            model.addAttribute("groupsCount", groupsCount);

            model.addAttribute("friendsCount", CollectionUtils.size(VkHelper.parseActiveUserFriends(userId)));
        }

        return "photo_search";
    }

    @GetMapping("/friends")
    public String findUserFriends(Model model, @RequestParam(name = "user_id") Optional<String> userIdParam) {
        if (userIdParam.isPresent()) {
            Integer userId = Integer.valueOf(userIdParam.get());
            UserXtrCounters user = VkHelper.parseUser(userId);
            model.addAttribute("user", user);

            List<Integer> friendIds = VkHelper.parseActiveUserFriends(userId);
            List<UserXtrCounters> friends = VkHelper.parseUsers(friendIds);
            model.addAttribute("friends", friends);
            model.addAttribute("friendsCount", CollectionUtils.size(friends));

            int photoCount = photoService.countByUserId(userId);
            model.addAttribute("photoCount", photoCount);

            int groupsCount = photoService.groupsCountByUserId(userId);
            model.addAttribute("groupsCount", groupsCount);
        }

        return "friends_search";
    }

}
