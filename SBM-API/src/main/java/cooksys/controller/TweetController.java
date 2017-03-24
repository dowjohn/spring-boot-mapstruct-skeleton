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
        TweetDtoOutput output = tweetService.getTweet(id);
        if (output != null) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
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
        TweetDtoOutput output = tweetService.deactivate(id, credentials);
        if (output != null) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

    }

    @RequestMapping(value = "/{id}/like", method = RequestMethod.PATCH)
    public boolean likeTweet(@PathVariable Long id, @RequestBody Credentials credentials, HttpServletResponse httpResponse) {
        boolean good = tweetService.likeTweet(id, credentials);
        if (good) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return good;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return good;
        }
    }

    @RequestMapping(value = "/{id}/reply", method = RequestMethod.POST)
    public TweetDtoOutput replyToTweet(@PathVariable Long id, @RequestBody TweetDtoSimpleInput tweetDtoSimpleInput, HttpServletResponse httpResponse) {
        TweetDtoOutput output = tweetService.replyToTweet(id, tweetDtoSimpleInput);
        if (output != null) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }


    @RequestMapping(value = "/{id}/repost", method = RequestMethod.POST)
    public TweetDtoOutput repostTweet(@PathVariable Long id, @RequestBody TweetDtoRepost tweetDtoRepost, HttpServletResponse httpResponse) {
        TweetDtoOutput output =  tweetService.repostTweet(id, tweetDtoRepost);
        if (output != null) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = "/{id}/tags", method = RequestMethod.GET)
    public List<HashtagDtoOutput> getTags(@PathVariable Long id, HttpServletResponse httpResponse) {
        List<HashtagDtoOutput> output = tweetService.getTags(id);
        if (output != null && !output.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = "/{id}/likes", method = RequestMethod.GET)
    public List<UserDtoOutput> getLikes(@PathVariable Long id, HttpServletResponse httpResponse) {
        List<UserDtoOutput> output =  tweetService.getLikes(id);
        if (output != null) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = "/{id}/replies", method = RequestMethod.GET)
    public List<TweetDtoOutput> getReplyTweets(@PathVariable Long id, HttpServletResponse httpResponse) {
        List<TweetDtoOutput> output = tweetService.getReplyTweets(id);
        if (output != null && !output.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = "/{id}/reposts", method = RequestMethod.GET)
    public List<TweetDtoOutput> getRepostsOfTweet(@PathVariable Long id, HttpServletResponse httpResponse) {
        List<TweetDtoOutput> output = tweetService.getRepostsOfTweet(id);
        if (output != null && !output.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = "/{id}/mentions", method = RequestMethod.GET)
    public List<UserDtoOutput> getMentionedUsers(@PathVariable Long id, HttpServletResponse httpResponse) {
        List<UserDtoOutput> output = tweetService.getMentionedUsers(id);
        if (output != null && !output.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

//    @RequestMapping(value = "/{id}/context", method = RequestMethod.GET)
//    public List<TweetDtoOutput> getContextTweets(@PathVariable Long id, HttpServletResponse httpResponse) {
//        return tweetService.getContextTweets(id);
//    }
}
