package com.tsimura.service.impl;

import com.tsimura.domain.Group;
import com.tsimura.domain.Photo;
import com.tsimura.repository.PhotoRepository;
import com.tsimura.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public Photo save(com.vk.api.sdk.objects.photos.Photo vkPhoto) {
        Photo photo = new Photo(
                vkPhoto.getId(),
                vkPhoto.getAlbumId(),
                vkPhoto.getOwnerId(),
                vkPhoto.getUserId(),
                vkPhoto.getPhoto130(),
                vkPhoto.getPhoto604()
        );
        return photoRepository.save(photo);
    }

    @Override
    public Collection<Photo> findAll() {
        return (Collection<Photo>) photoRepository.findAll();
    }

    @Override
    public Photo findOne(int photoId) {
        return photoRepository.findOne(photoId);
    }

    @Override
    public Integer countByUserId(int userId) {
        return photoRepository.countByUserId(userId);
    }

    @Override
    public Long countByOwnerId(int ownerId) {
        return photoRepository.countByOwnerId(ownerId);
    }

    @Override
    public Long countByGroup(Group group) {
        int ownerId = - Integer.valueOf(group.getId());
        return countByOwnerId(ownerId);
    }

    @Override
    public Integer groupsCountByUserId(int userId) {
        return photoRepository.groupsCountByUserId(userId);
    }

    @Override
    public boolean isUserPresent(int userId) {
        return countByUserId(userId) > 0;
    }

    @Override
    public List<Photo> findByUserId(int userId) {
        return photoRepository.findByUserId(userId);
    }

    @Override
    public List<String> findIdByUserId(int userId) {
        List<String> stringIds = new ArrayList<>();
        for (int id : photoRepository.findIdByUserId(userId)) stringIds.add(String.valueOf(id));
        return stringIds;
    }

}
