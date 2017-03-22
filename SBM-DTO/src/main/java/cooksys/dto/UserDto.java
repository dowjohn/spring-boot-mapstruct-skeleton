package cooksys.dto;

import cooksys.entity.Credentials;
import cooksys.entity.Profile;
import cooksys.entity.Tweet;
import cooksys.entity.User;

import java.util.Date;
import java.util.Set;

public class UserDto {
    private Long id;

    private boolean isActive;

    private Profile profile;

    private Credentials credentials;

    private Set<Tweet> usersTweets;

    private Set<Tweet> mentionedIn;

    private Set<User> following;

    private User leader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
