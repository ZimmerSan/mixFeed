package com.tsimura.web.controller.social;

import com.tsimura.service.PhotoService;
import com.tsimura.service.SecurityService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoAlbumFull;
import com.vk.api.sdk.objects.photos.responses.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final UsersConnectionRepository connectionRepository;
    private final SecurityService securityService;
    private final PhotoService photoService;

    @Autowired
    public SocialController(UsersConnectionRepository connectionRepository,
                            SecurityService securityService,
                            PhotoService photoService) {
        this.connectionRepository = connectionRepository;
        this.securityService = securityService;
        this.photoService = photoService;
    }

    @GetMapping("")
    public String rootSocial(Model model, @RequestParam Optional<String> provider, @RequestParam Optional<String> status) {
        if (provider.isPresent() && status.get().equalsIgnoreCase("ok"))
            model.addAttribute("success", provider.get());

        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());

        try {
            Integer GROUP_ID = 142125218;
            parseGroup(GROUP_ID.toString());

            Integer USER_ID = 352247988;
            parseUser(USER_ID.toString());
        } catch (ApiException | ClientException e) {
            log.error("social exception: ", e);
        }

        return "social/social";
    }

    public String parseGroup(String groupIdParam) throws ClientException, ApiException {
        Integer groupId = Integer.valueOf(groupIdParam);
        if (groupId > 0) groupId = -groupId;

        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        List<PhotoAlbumFull> albumList = vk.photos().getAlbums().ownerId(groupId).execute().getItems();
        for (PhotoAlbumFull album : albumList) {
            GetResponse photos = vk.photos().get().ownerId(groupId).albumId(album.getId().toString()).execute();
            List<Photo> photoList = photos.getItems();
            for (Photo photo : photoList) photoService.save(photo);
        }

        return null;
    }

    public String parseUser(String userIdParam) throws ClientException, ApiException {
        Integer userId = Integer.valueOf(userIdParam);

        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        List<String> photoIds = photoService.findIdByUserId(userId);
        log.debug("photoIds = {}", photoIds);
//        GetResponse execute = vk.photos().get().photoIds(photoIds).execute();
//        for (Photo photo : execute.getItems()) {
//            log.debug("photo = {}", photo);
//        }
        return null;
    }


}
