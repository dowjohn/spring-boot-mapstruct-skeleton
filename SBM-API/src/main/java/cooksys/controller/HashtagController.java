package cooksys.controller;

import cooksys.dto.HashtagDtoOutput;
import cooksys.dto.TweetDtoOutput;
import cooksys.service.HashtagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("tags")
@Api(tags = {"public", "hashtags"})
public class HashtagController {

    @Autowired
    private HashtagService hashtagService;

    @GetMapping
    @ApiOperation(value = "", nickname = "getAllTags")
    public List<HashtagDtoOutput> getAll() {
        return hashtagService.getAll();
    }

    @RequestMapping(value = "/{label}", method = RequestMethod.GET)
    public List<TweetDtoOutput> getTweets(@PathVariable String label, HttpServletResponse response) {
        List<TweetDtoOutput> output = hashtagService.getTweetsByHashtag(label);
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
