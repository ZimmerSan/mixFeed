package com.tsimura.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "groups")
public class Group {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "screen_name")
    private String screenName;

    @Column(name = "is_closed")
    private int isClosed;

    @Column(name = "photo_200")
    private String photo200;

    private Long photosCount;

    public Group() {
    }

    public Group(String id, String name, String screenName, int isClosed, String photo200) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.isClosed = isClosed;
        this.photo200 = photo200;
    }
}
