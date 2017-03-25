package com.tsimura.web.controller;

import com.tsimura.domain.Photo;
import com.tsimura.domain.security.CurrentUser;
import com.tsimura.service.PhotoService;
import com.tsimura.service.SecurityService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.friends.responses.GetResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
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
            Integer userId = Integer.valueOf(userIdParam.get());
            UserXtrCounters user = parseUser(userId);
            model.addAttribute("user", user);

            List<Photo> photos = photoService.findByUserId(userId);
            model.addAttribute("photos", photos);
        }

        return "photo_search";
    }

    @GetMapping("/friends")
    public String findUserFriends(Model model, @RequestParam(name = "user_id") Optional<String> userIdParam) {
        if (userIdParam.isPresent()) {
            Integer userId = Integer.valueOf(userIdParam.get());

            UserXtrCounters user = parseUser(userId);
            model.addAttribute("user", user);

            List<Integer> friendIds = parseActiveUserFriends(userId);
            List<UserXtrCounters> friends = parseUsers(friendIds);
            model.addAttribute("friends", friends);
        }

        return "friends_search";
    }


    private List<Integer> parseActiveUserFriends(Integer userId) {
        List<Integer> activeFriends = new ArrayList<>();
        List<Integer> friends = parseUserFriends(userId);
        if (friends != null)
            for (Integer friend : friends) if (photoService.isUserPresent(friend)) activeFriends.add(friend);
        return activeFriends;
    }

    private List<Integer> parseUserFriends(Integer userId) {
        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        try {
            GetResponse response = vk.friends().get().userId(userId).execute();
            return response.getItems();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<UserXtrCounters> parseUsers(List<Integer> userIds) {
        List<String> userIdsStr = new ArrayList<>(userIds.size());
        for (Integer userId : userIds) userIdsStr.add(userId.toString());

        try {
            VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
            List<UserXtrCounters> users = vk.users().get().userIds(userIdsStr).fields(UserField.PHOTO_200, UserField.DOMAIN).execute();
            return users;
        } catch (ApiException | ClientException e) {
            log.error("parseUsers error: ", e);
        }
        return null;
    }

    private UserXtrCounters parseUser(Integer userId) {
        try {
            VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
            List<UserXtrCounters> users = vk.users().get().userIds(userId.toString()).fields(UserField.PHOTO_200, UserField.DOMAIN).execute();
            return users.iterator().next();
        } catch (ApiException | ClientException e) {
            log.error("parseUser error: ", e);
        }
        return null;
    }

}
