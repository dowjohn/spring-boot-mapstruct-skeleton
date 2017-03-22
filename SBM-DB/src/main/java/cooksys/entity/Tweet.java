package cooksys.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Tweet parent;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parent")
    private List<Tweet> reposts;

    @ManyToMany(mappedBy = "tweetsWithHashtag")
    private List<Hashtag> hashtags;

    @ManyToMany
    private List<User> mentions;
}
