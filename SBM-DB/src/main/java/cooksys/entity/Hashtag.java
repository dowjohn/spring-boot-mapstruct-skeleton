package cooksys.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String label;

    @ManyToMany
    private List<Tweet> tweetsWithHashtag;
}
