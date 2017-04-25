package com.tsimura.service;

import com.tsimura.domain.Group;
import com.tsimura.domain.Photo;

import java.util.Collection;
import java.util.List;

public interface PhotoService {

    Photo save(com.vk.api.sdk.objects.photos.Photo photo);

    Collection<Photo> findAll();

    Photo findOne(int photoId);

    Integer countByUserId(int userId);

    Long countByOwnerId(int ownerId);

    Long countByGroup(Group group);

    Integer groupsCountByUserId(int userId);

    boolean isUserPresent(int userId);

    List<Photo> findByUserId(int userId);

    List<String> findIdByUserId(int userId);

}
