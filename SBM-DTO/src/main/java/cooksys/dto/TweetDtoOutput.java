package cooksys.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TweetDtoOutput {

    private Long id;

    private String author;

    private Date posted;

    private String content;

    private Long replyTo;

    private Long repostOf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public Long getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
    }

    public Long getRepostOf() {
        return repostOf;
    }

    public void setRepostOf(Long repostOf) {
        this.repostOf = repostOf;
    }
}
