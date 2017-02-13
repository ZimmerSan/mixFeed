package com.tsimura.domain.social;

import com.tsimura.domain.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class SocialAccount implements Serializable {
    @Id
    private String id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
    private User owner;

    public SocialAccount(String id) {
        this.id = id;
    }

}
