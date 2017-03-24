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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Tweet originalTweetReply;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "originalTweetReply")
    private List<Tweet> replies;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Tweet parentTweetRepost;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentTweetRepost")
    private List<Tweet> reposts;

    @ManyToMany
    private List<Hashtag> hashtags;

    @ManyToMany
    private List<User> mentions;

    @ManyToMany
    private List<User> likedIt;

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

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Tweet getOriginalTweetReply() {
        return originalTweetReply;
    }

    public void setOriginalTweetReply(Tweet originalTweetReply) {
        this.originalTweetReply = originalTweetReply;
    }

    public List<Tweet> getReplies() {
        return replies;
    }

    public void setReplies(List<Tweet> replies) {
        this.replies = replies;
    }

    public Tweet getParentTweetRepost() {
        return parentTweetRepost;
    }

    public void setParentTweetRepost(Tweet parentTweetRepost) {
        this.parentTweetRepost = parentTweetRepost;
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

    public List<User> getLikedIt() {
        return likedIt;
    }

    public void setLikedIt(List<User> likedIt) {
        this.likedIt = likedIt;
    }
}
