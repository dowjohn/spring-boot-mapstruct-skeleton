package cooksys.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "peep")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(name="created_at", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = new Date();
    }

    private Profile profile;

    private Credentials credentials;

    @OneToMany(mappedBy = "author")
    private List<Tweet> usersTweets;

    @ManyToMany(mappedBy = "mentions")
    private List<Tweet> mentionedIn;

    @ManyToMany
    private List<User> leaders;

    @ManyToMany(mappedBy = "leaders")
    private List<User> followers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public List<Tweet> getUsersTweets() {
        return usersTweets;
    }

    public void setUsersTweets(List<Tweet> usersTweets) {
        this.usersTweets = usersTweets;
    }

    public List<Tweet> getMentionedIn() {
        return mentionedIn;
    }

    public void setMentionedIn(List<Tweet> mentionedIn) {
        this.mentionedIn = mentionedIn;
    }

    public List<User> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<User> leaders) {
        this.leaders = leaders;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}