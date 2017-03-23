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

    @ManyToMany(mappedBy = "hashtags")
    private List<Tweet> tweetsWithHashtag;

    public Hashtag(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Tweet> getTweetsWithHashtag() {
        return tweetsWithHashtag;
    }

    public void setTweetsWithHashtag(List<Tweet> tweetsWithHashtag) {
        this.tweetsWithHashtag = tweetsWithHashtag;
    }
}
