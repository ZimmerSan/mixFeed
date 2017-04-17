package com.tsimura.web.controller.rest;

import com.tsimura.domain.Photo;
import com.tsimura.service.PhotoService;
import com.tsimura.web.VkHelper;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final PhotoService photoService;

    @Autowired
    public UserRestController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/{userId}")
    UserFull readUser(@PathVariable String userId) {
        return VkHelper.parseUser(userId);
    }

    @GetMapping("/{userId}/friends/active")
    List<UserXtrCounters> readUserActiveFriends(@PathVariable Integer userId) {
        List<Integer> friendIds = VkHelper.parseActiveUserFriends(userId);
        List<UserXtrCounters> friends = VkHelper.parseUsers(friendIds);
        return friends;
    }

    @GetMapping("/{userId}/photos")
    List<Photo> readUserPhotos(@PathVariable Integer userId) {
        return photoService.findByUserId(userId);
    }

    @GetMapping("/{userId}/photos/count")
    Integer readUserPhotosCount(@PathVariable Integer userId) {
        return photoService.countByUserId(userId);
    }

    @GetMapping("/{userId}/groups")
    List<Photo> readUserGroups(@PathVariable Integer userId) {
        return photoService.findByUserId(userId);
    }

    @GetMapping("/{userId}/groups/count")
    Integer readUserGroupsCount(@PathVariable Integer userId) {
        return photoService.groupsCountByUserId(userId);
    }

}
