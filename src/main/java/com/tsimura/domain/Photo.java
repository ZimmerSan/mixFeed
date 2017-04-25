package com.tsimura.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    private int id;

    @Column(name = "album_id")
    private int albumId;

    @Column(name = "owner_id")
    private int ownerId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "photo_130")
    private String photo130;
    
    @Column(name = "photo_604")
    private String photo604;

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
