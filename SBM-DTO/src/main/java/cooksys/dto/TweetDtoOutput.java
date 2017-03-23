package cooksys.dto;

import java.util.Date;

/**
 * Created by student-2 on 3/22/2017.
 */
public class TweetDtoOutput {
    private Long id;
    private String author;
    private Date posted;
    private String content;
    private Long inReplyTo;
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

    public Long getInReplyTo() {
        return inReplyTo;
    }

    public void setInReplyTo(Long inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    public Long getRepostOf() {
        return repostOf;
    }

    public void setRepostOf(Long repostOf) {
        this.repostOf = repostOf;
    }
}
