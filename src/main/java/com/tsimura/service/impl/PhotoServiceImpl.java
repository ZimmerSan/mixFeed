package com.tsimura.service.impl;

import com.tsimura.domain.Photo;
import com.tsimura.repository.PhotoRepository;
import com.tsimura.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Long countByUserId(int userId) {
        return photoRepository.countByUserId(userId);
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