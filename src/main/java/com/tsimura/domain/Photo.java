package com.tsimura.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    int id;

    @Column(name = "album_id")
    int albumId;

    @Column(name = "owner_id")
    int ownerId;

    @Column(name = "user_id")
    int userId;

    @Column(name = "photo_130")
    String photo130;
    
    @Column(name = "photo_604")
    String photo604;

    public Photo() {
    }

    public Photo(int id, int albumId, int ownerId, int userId, String photo130, String photo604) {
        this.id = id;
        this.albumId = albumId;
        this.ownerId = ownerId;
        this.userId = userId;
        this.photo130 = photo130;
        this.photo604 = photo604;
    }
}
