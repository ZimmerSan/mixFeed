package com.tsimura.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    int id;

    int albumId;
    int ownerId;
    int userId;

    String photo130;
    String photo604;

    public Photo(){}

    public Photo(int id, int albumId, int ownerId, int userId, String photo130, String photo604) {
        this.id = id;
        this.albumId = albumId;
        this.ownerId = ownerId;
        this.userId = userId;
        this.photo130 = photo130;
        this.photo604 = photo604;
    }
}
