package cooksys.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Hashtag {
    @Id
    @GeneratedValue
    private Long id;

    private String label;

    @ManyToMany
    private Set<Tweet> tweetsWithHashtag;
}
