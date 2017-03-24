package cooksys.mapper;

import cooksys.dto.TweetDtoOutput;
import cooksys.dto.TweetDtoRepost;
import cooksys.dto.TweetDtoSimpleInput;
import cooksys.entity.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TweetMapper {
    @Mappings({
            @Mapping(source = "id",target="id"),
            @Mapping(source = "author.id", target = "author"),
            @Mapping(source = "posted", target = "posted"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "originalTweetReply.id", target = "replyTo"),
            @Mapping(source = "parentTweetRepost.id", target = "repostOf")
    })
    TweetDtoOutput toTweetDtoOutput(Tweet tweet);

    @Mappings({
            @Mapping(source = "content", target = "content")
    })
    Tweet toTweet(TweetDtoSimpleInput input);

    Tweet toTweetFromRepost(TweetDtoRepost tweetDtoRepost);
}
