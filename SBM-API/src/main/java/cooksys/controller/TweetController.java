package cooksys.controller;

import cooksys.dto.*;
import cooksys.entity.Credentials;
import cooksys.service.TweetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by student-2 on 3/21/2017.
 */
@RestController
@Validated
@RequestMapping("tweets")
@Api(tags = {"public", "tweets"})
public class TweetController {
    @Autowired
    private TweetService tweetService;

    @GetMapping
    @ApiOperation(value = "", nickname = "getAllTweets")
    public List<TweetDtoOutput> getAll() {
        return tweetService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TweetDtoOutput getTweet(@PathVariable Long id, HttpServletResponse httpResponse) {
        return tweetService.getTweet(id);
    }


    @PostMapping
    @ApiOperation(value = "", nickname = "createSimpleTweet")
    public TweetDtoOutput post(@RequestBody TweetDtoSimpleInput tweetDtoSimpleInput, HttpServletResponse httpResponse) {
        TweetDtoOutput output = tweetService.post(tweetDtoSimpleInput);
        if (output != null) {
            httpResponse.setStatus(HttpServletResponse.SC_CREATED);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    // really a patch tho
    @DeleteMapping("{id}")
    @ApiOperation(value = "", nickname = "deactivateTweet")
    public TweetDtoOutput delete(@PathVariable Long id, @RequestBody Credentials credentials, HttpServletResponse httpResponse) {
        return tweetService.deactivate(id, credentials);
    }


    @RequestMapping(value = "/{id}/like", method = RequestMethod.PATCH)
    public boolean likeTweet(@PathVariable Long id, @RequestBody Credentials credentials, HttpServletResponse httpResponse) {
        return tweetService.likeTweet(id, credentials);
    }

    @RequestMapping(value = "/{id}/reply", method = RequestMethod.POST)
    public TweetDtoOutput replyToTweet(@PathVariable Long id, @RequestBody TweetDtoSimpleInput tweetDtoSimpleInput, HttpServletResponse httpResponse) {
        return tweetService.replyToTweet(id, tweetDtoSimpleInput);
    }


    @RequestMapping(value = "/{id}/repost", method = RequestMethod.POST)
    public TweetDtoOutput repostTweet(@PathVariable Long id, @RequestBody TweetDtoRepost tweetDtoRepost, HttpServletResponse httpResponse) {
        return tweetService.repostTweet(id, tweetDtoRepost);
    }

    @RequestMapping(value = "/{id}/tags", method = RequestMethod.GET)
    public List<HashtagDtoOutput> getTags(@PathVariable Long id, HttpServletResponse httpResponse) {
        return tweetService.getTags(id);
    }

    @RequestMapping(value = "/{id}/likes", method = RequestMethod.GET)
    public List<UserDtoOutput> getLikes(@PathVariable Long id, HttpServletResponse httpResponse) {
        return tweetService.getLikes(id);
    }

    @RequestMapping(value = "/{id}/replies", method = RequestMethod.GET)
    public List<TweetDtoOutput> getReplyTweets(@PathVariable Long id, HttpServletResponse httpResponse) {
        return tweetService.getReplyTweets(id);
    }

    @RequestMapping(value = "/{id}/reposts", method = RequestMethod.GET)
    public List<TweetDtoOutput> getRepostsOfTweet(@PathVariable Long id, HttpServletResponse httpResponse) {
        return tweetService.getRepostsOfTweet(id);
    }

    @RequestMapping(value = "/{id}/mentions", method = RequestMethod.GET)
    public List<UserDtoOutput> getMentionedUsers(@PathVariable Long id, HttpServletResponse httpResponse) {
        return tweetService.getMentionedUsers(id);
    }

//    @RequestMapping(value = "/{id}/context", method = RequestMethod.GET)
//    public List<TweetDtoOutput> getContextTweets(@PathVariable Long id, HttpServletResponse httpResponse) {
//        return tweetService.getContextTweets(id);
//    }
}
