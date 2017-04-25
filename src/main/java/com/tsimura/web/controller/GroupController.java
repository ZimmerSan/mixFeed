package com.tsimura.web.controller;

import com.tsimura.domain.Group;
import com.tsimura.domain.Photo;
import com.tsimura.service.GroupService;
import com.tsimura.service.PhotoService;
import com.tsimura.service.SecurityService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.photos.PhotoAlbumFull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Slf4j
@Controller
public class GroupController {

    private final UsersConnectionRepository connectionRepository;
    private final GroupService groupService;
    private final PhotoService photoService;
    private final SecurityService securityService;

    @Autowired
    public GroupController(UsersConnectionRepository connectionRepository, GroupService groupService, PhotoService photoService, SecurityService securityService) {
        this.connectionRepository = connectionRepository;
        this.groupService = groupService;
        this.photoService = photoService;
        this.securityService = securityService;
    }

    @GetMapping("/groups")
    public String getGroups(Model model) {
        List<Group> groups = groupService.findAll();
        model.addAttribute("groups", groups);

        return "manage_groups";
    }

    @PostMapping("/groups")
    public String processGroup(Model model, @RequestParam(name = "group_id") Optional<String> groupIdParam) {
        if (groupIdParam.isPresent()) {
            scanGroups(Collections.singletonList(groupIdParam.get()));
        }
        return getGroups(model);
    }

    void scanGroups(List<String> groupIds) {
        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        try {
            List<GroupFull> execute = vk.groups().getById().groupIds(groupIds).execute();
            for (GroupFull groupFull : execute) {
                groupService.save(groupFull);
                List<Photo> photos = scanGroupPhotos(groupFull.getId());
                log.debug("photos.size() = {}", photos.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Photo> scanGroupPhotos(String groupIdParam) throws ClientException {
        List<Photo> scannedPhotos = new ArrayList<>();
        Integer groupId = Integer.valueOf(groupIdParam);
        if (groupId > 0) groupId = -groupId;

        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        try {
            List<PhotoAlbumFull> albumList = vk.photos().getAlbums().ownerId(groupId).execute().getItems();
            for (PhotoAlbumFull album : albumList) {
                List<com.vk.api.sdk.objects.photos.Photo> photoList = vk.photos().get().ownerId(groupId).albumId(album.getId().toString()).execute().getItems();
                for (com.vk.api.sdk.objects.photos.Photo photo : photoList) scannedPhotos.add(photoService.save(photo));
            }
        } catch (ApiException e) {
            log.error("Error", e);
        }

//        try {
//            if (true) {
//                List<com.vk.api.sdk.objects.photos.Photo> photoList = vk.photos().get().ownerId(groupId).albumId("wall").execute().getItems();
//                for (com.vk.api.sdk.objects.photos.Photo photo : photoList) scannedPhotos.add(photoService.save(photo));
//            }
//        } catch (ApiException e) {
//           log.error("Error", e);
//        }
        return scannedPhotos;
    }
}