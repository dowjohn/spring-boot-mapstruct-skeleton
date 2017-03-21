package cooksys.dto;

import cooksys.entity.Credentials;
import cooksys.entity.Profile;
import cooksys.entity.Tweet;
import cooksys.entity.User;

import java.sql.Timestamp;
import java.util.Set;

public class UserDto {
    private Long id;

    private String username;

    private boolean isActive;

    private Timestamp timestamp;

    private Profile profile;

    private Credentials credentials;

    private Set<Tweet> usersTweets;

    private Set<Tweet> mentionedIn;

    private Set<User> following;

    private User leader;

    public UserDto(){}

    // new user
    public UserDto(String username, boolean isActive, Profile profile, Credentials credentials) {
        this.username = username;
        this.isActive = isActive;
        this.profile = profile;
        this.credentials = credentials;
    }

    //from db
    public UserDto(Long id, String username, boolean isActive, Timestamp timestamp, Profile profile, Credentials credentials, Set<Tweet> usersTweets, Set<Tweet> mentionedIn, Set<User> following, User leader) {
        this.id = id;
        this.username = username;
        this.isActive = isActive;
        this.timestamp = timestamp;
        this.profile = profile;
        this.credentials = credentials;
        this.usersTweets = usersTweets;
        this.mentionedIn = mentionedIn;
        this.following = following;
        this.leader = leader;
    }
}
