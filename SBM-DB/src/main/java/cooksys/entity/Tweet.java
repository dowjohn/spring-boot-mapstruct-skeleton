package cooksys.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User author;

    private boolean isAlive;

    private Timestamp postDate;

    private String content;

    @OneToOne
    private Tweet inReplyTo;

    @OneToOne(mappedBy = "inReplyTo")
    private Tweet original;

//    @OneToMany(mappedBy = "reposted")
//    private Tweet repostOf;
//
//    @ManyToOne
//    private Set<Tweet> reposted;

    @ManyToMany(mappedBy = "tweetsWithHashtag")
    private Set<Hashtag> hashtags;

    @ManyToMany(mappedBy = "mentionedIn")
    private Set<User> mentions;
}
