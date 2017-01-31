package com.tsimura.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class User {

    @Id @GeneratedValue private Long id;
    private String username;

    public User(){
        this("unknown");
    }

    public User(String username){
        this.username = username;
    }
}
