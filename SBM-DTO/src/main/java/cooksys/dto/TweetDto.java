package cooksys.dto;

import cooksys.entity.Hashtag;
import cooksys.entity.Tweet;
import cooksys.entity.User;

import java.sql.Timestamp;
import java.util.Set;

public class TweetDto {
    private Long id;

    private User author;

    private Timestamp postDate;

    private String content;

    private Tweet inReplyTo;

    private Tweet repostOf;

    private Set<Hashtag> hashtags;

    private Set<User> mentions;
}
