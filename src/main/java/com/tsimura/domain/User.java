package com.tsimura.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {

    public static final String DEFAULT_USERNAME = "unknown";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
//    private Set<FacebookAccount> myFacebookAccounts = new HashSet<>();

    public User() {
        this(DEFAULT_USERNAME);
    }

    public User(String username) {
        this(username, null);
    }

    public User(String username, String password){
        this.setUsername(username);
        this.setPassword(password);
    }

}
