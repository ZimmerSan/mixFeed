package com.tsimura.web.controller.rest;

import com.tsimura.domain.Group;
import com.tsimura.domain.Photo;
import com.tsimura.service.GroupService;
import com.tsimura.service.PhotoService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.photos.PhotoAlbumFull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/groups")
public class GroupRestController {

    private final GroupService groupService;
    private final PhotoService photoService;

    @Autowired
    public GroupRestController(GroupService groupService, PhotoService photoService) {
        this.groupService = groupService;
        this.photoService = photoService;
    }

    @GetMapping
    List<Group> readGroups() {
        List<Group> groups = groupService.findAll();
        groups.forEach(group -> group.setPhotosCount(photoService.countByGroup(group)));
        return groups;
    }

    @PutMapping("/{groupId}")
    Group updateGroup(@PathVariable Integer groupId) throws Exception {
        List<Photo> scannedPhotos = new ArrayList<>();
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

        Group group = groupService.findById(String.valueOf(-groupId)).get();
        group.setPhotosCount(photoService.countByGroup(group));
        log.debug("group = {}", group);
        return group;
    }

}
