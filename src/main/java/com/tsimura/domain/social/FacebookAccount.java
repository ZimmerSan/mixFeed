package com.tsimura.domain.social;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "facebook")
public class FacebookAccount extends SocialAccount {

    private FacebookAccount(){
        super();
    }

    public FacebookAccount(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return "FacebookAccount: " + super.toString();
    }
}
