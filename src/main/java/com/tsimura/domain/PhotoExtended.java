package com.tsimura.domain;

import com.vk.api.sdk.objects.users.UserFull;
import lombok.Data;

@Data
public class PhotoExtended extends Photo {

    private Group group;
    private UserFull user;

    public PhotoExtended(Photo photo, Group group, UserFull user) {
        super(photo.getId(), photo.getAlbumId(), photo.getOwnerId(), photo.getUserId(), photo.getPhoto130(), photo.getPhoto604());
        this.group = group;
        this.user = user;
    }

}
