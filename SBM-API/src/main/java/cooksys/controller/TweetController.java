package cooksys.controller;

import cooksys.dto.TweetDtoOutput;
import cooksys.dto.TweetDtoSimpleInput;
import cooksys.entity.Tweet;
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


    @PostMapping
    @ApiOperation(value = "", nickname = "createSimpleTweet")
    public TweetDtoOutput post(@RequestBody TweetDtoSimpleInput tweetDtoSimpleInput, HttpServletResponse httpResponse) {
        TweetDtoOutput output = tweetService.post(tweetDtoSimpleInput);
        httpResponse.setStatus(HttpServletResponse.SC_CREATED);
        return output;
    }


}
