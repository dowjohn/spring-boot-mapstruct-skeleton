package cooksys.mapper;

import cooksys.dto.TweetDtoOutput;
import cooksys.dto.TweetDtoSimpleInput;
import cooksys.entity.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Created by student-2 on 3/22/2017.
 */

@Mapper(componentModel = "spring")
public interface TweetMapper {
    @Mappings({
            @Mapping(source = "id",target="id"),
            @Mapping(source = "author.id", target = "author"),
            @Mapping(source = "posted", target = "posted"),
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "inReplyTo.id", target = "inReplyTo"),
            @Mapping(source = "parent.id", target = "repostOf")
    })
    TweetDtoOutput toTweetDtoOutput(Tweet tweet);

    @Mappings({
            @Mapping(source = "content", target = "content")
    })
    Tweet toTweet(TweetDtoSimpleInput input);
}