package cooksys.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User author;

    private boolean isAlive = true;

    @Column(name="post_date", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date posted;

    @PrePersist
    protected void onCreate() {
        posted = new Date();
    }

    private String content;

    @OneToOne
    private Tweet inReplyTo;

    @OneToOne(mappedBy = "inReplyTo")
    private Tweet original;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Tweet parent;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parent")
    private List<Tweet> reposts;

    @ManyToMany
    private List<Hashtag> hashtags;

    @ManyToMany
    private List<User> mentions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date postDate) {
        this.posted = posted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Tweet getInReplyTo() {
        return inReplyTo;
    }

    public void setInReplyTo(Tweet inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    public Tweet getOriginal() {
        return original;
    }

    public void setOriginal(Tweet original) {
        this.original = original;
    }

    public Tweet getParent() {
        return parent;
    }

    public void setParent(Tweet parent) {
        this.parent = parent;
    }

    public List<Tweet> getReposts() {
        return reposts;
    }

    public void setReposts(List<Tweet> reposts) {
        this.reposts = reposts;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public List<User> getMentions() {
        return mentions;
    }

    public void setMentions(List<User> mentions) {
        this.mentions = mentions;
    }
}
