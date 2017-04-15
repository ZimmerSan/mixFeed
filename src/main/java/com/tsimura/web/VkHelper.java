package com.tsimura.web;

import com.tsimura.service.PhotoService;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.friends.responses.GetResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class VkHelper {

    private static PhotoService photoService;

    @Autowired
    private PhotoService privatePhotoService;

    // workaround for static service use
    @PostConstruct
    public void init() {
        photoService = privatePhotoService;
    }

    public static List<Integer> parseActiveUserFriends(Integer userId) {
        List<Integer> activeFriends = new ArrayList<>();
        List<Integer> friends = parseUserFriends(userId);
        if (friends != null)
            for (Integer friend : friends) if (photoService.isUserPresent(friend)) activeFriends.add(friend);
        return activeFriends;
    }

    public static List<Integer> parseUserFriends(Integer userId) {
        VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
        try {
            GetResponse response = vk.friends().get().userId(userId).execute();
            return response.getItems();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UserXtrCounters> parseUsers(List<Integer> userIds) {
        List<String> userIdsStr = new ArrayList<>(userIds.size());
        for (Integer userId : userIds) userIdsStr.add(userId.toString());

        try {
            VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
            List<UserXtrCounters> users = vk.users().get().userIds(userIdsStr).fields(UserField.PHOTO_50, UserField.PHOTO_100, UserField.PHOTO_200, UserField.DOMAIN).execute();
            return users;
        } catch (ApiException | ClientException e) {
            log.error("parseUsers error: ", e);
        }
        return null;
    }

    public static UserXtrCounters parseUser(String userId) {
        try {
            VkApiClient vk = new VkApiClient(HttpTransportClient.getInstance());
            List<UserXtrCounters> users = vk.users().get().userIds(userId).fields(UserField.PHOTO_200, UserField.DOMAIN, UserField.SEX).execute();
            return users.iterator().next();
        } catch (ApiException | ClientException e) {
            log.error("parseUser error: ", e);
        }
        return null;
    }

}
