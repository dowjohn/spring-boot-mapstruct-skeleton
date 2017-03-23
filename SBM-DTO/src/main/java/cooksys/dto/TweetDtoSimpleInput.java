package cooksys.dto;

import cooksys.entity.Credentials;
import cooksys.entity.Hashtag;
import cooksys.entity.User;

import java.util.HashSet;
import java.util.Set;

public class TweetDtoSimpleInput {
    private String content;
    private Credentials credentials;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
