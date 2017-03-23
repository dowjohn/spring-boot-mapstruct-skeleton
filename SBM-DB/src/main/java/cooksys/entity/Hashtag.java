package cooksys.entity;

import javax.persistence.*;
import java.util.Date;
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

    @Column(name="first_date", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date first;

    @Column(name="last_date", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date last;

    @PrePersist
    protected void createOn() {
        last = new Date();
        first = new Date();
    }

    public Hashtag() {}

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

    public Date getFirst() {
        return first;
    }

    public void setFirst(Date first) {
        this.first = first;
    }

    public Date getLast() {
        return last;
    }

    public void setLast(Date last) {
        this.last = last;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hashtag hashtag = (Hashtag) o;

        return id != null ? id.equals(hashtag.id) : hashtag.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
