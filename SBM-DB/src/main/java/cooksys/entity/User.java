package cooksys.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private Timestamp timestamp;

    private Profile profile;

    private Credentials credentials;

    @OneToMany(mappedBy = "author")
    private Set<Tweet> usersTweets;

    @ManyToMany
    private Set<Tweet> mentionedIn;

    @ManyToOne
    private User leader;

    @OneToMany(mappedBy = "leader")
    private Set<User> following;


}