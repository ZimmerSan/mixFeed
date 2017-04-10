package com.tsimura.web.controller.rest;


import com.tsimura.domain.Photo;
import com.tsimura.domain.PhotoExtended;
import com.tsimura.service.PhotoExtendedService;
import com.tsimura.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/photos")
public class PhotoRestController {

    private final PhotoService photoService;
    private final PhotoExtendedService photoExtendedService;

    @Autowired
    public PhotoRestController(PhotoService photoService, PhotoExtendedService photoExtendedService) {
        this.photoService = photoService;
        this.photoExtendedService = photoExtendedService;
    }

    @GetMapping
    Collection<Photo> getPhotos() {
        return this.photoService.findAll();
    }

    @GetMapping("/{photoId}")
    Photo getPhoto(@PathVariable Integer photoId) {
        return this.photoService.findOne(photoId);
    }

    @GetMapping("/{photoId}/extended")
    PhotoExtended getPhotoExtended(@PathVariable Integer photoId) {
        return this.photoExtendedService.findOne(photoId);
    }

}
