package cooksys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private boolean isActive;

    private Timestamp timestamp;

    private Profile profile;

    private Credentials credentials;

    private Set<Tweet> usersTweets;
}