package com.tsimura.service.impl;

import com.tsimura.domain.Group;
import com.tsimura.domain.Photo;
import com.tsimura.domain.PhotoExtended;
import com.tsimura.repository.GroupRepository;
import com.tsimura.repository.PhotoRepository;
import com.tsimura.service.PhotoExtendedService;
import com.tsimura.web.VkHelper;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.springframework.stereotype.Service;

@Service
public class PhotoExtendedServiceImpl implements PhotoExtendedService {

    private final PhotoRepository photoRepository;
    private final GroupRepository groupRepository;

    public PhotoExtendedServiceImpl(PhotoRepository photoRepository, GroupRepository groupRepository) {
        this.photoRepository = photoRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public PhotoExtended findOne(int photoId) {
        Photo photo = photoRepository.findOne(photoId);
        int groupId = photo.getOwnerId();
        if (groupId < 0) {
            groupId = - groupId;
            Group group = groupRepository.findOne(String.valueOf(groupId));
            UserXtrCounters user = VkHelper.parseUser(photo.getUserId());
            return new PhotoExtended(photo, group, user);
        }
        return null;
    }
}
