package cooksys.controller;

import cooksys.dto.TweetDtoOutput;
import cooksys.dto.UserDtoCreate;
import cooksys.dto.UserDtoOutput;
import cooksys.entity.Credentials;
import cooksys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Validated
@RequestMapping("users")
@Api(tags = {"public", "users"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation(value = "", nickname = "getAllUsers")
    public List<UserDtoOutput> index() {
        return userService.index();
    }

    @GetMapping("{username}")
    @ApiOperation(value = "", nickname = "getUserByName")
    public UserDtoOutput getUserByName(@RequestParam(value="username") String username, HttpServletResponse response) {
        UserDtoOutput output = userService.getUserByName(username.replace("@", ""));
        if (output != null) {
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }

    @PostMapping
    @ApiOperation(value = "", nickname = "createUser")
    public UserDtoOutput post(@RequestBody UserDtoCreate userDtoCreate, HttpServletResponse httpResponse) {
        UserDtoOutput output = userService.post(userDtoCreate);
        httpResponse.setStatus(HttpServletResponse.SC_CREATED);
        return output;
    }


    @PatchMapping("{username}")
    @ApiOperation(value = "", nickname = "updateUser")
    public UserDtoOutput patch(@PathVariable String username, @RequestBody UserDtoCreate userDto, HttpServletResponse httpResponse) {
        return userService.patch(username.replace("@", ""), userDto);
    }

    // really a patch tho
    @DeleteMapping("{username}")
    @ApiOperation(value = "", nickname = "deactivateUser")
    public UserDtoOutput delete(@PathVariable String username, HttpServletResponse httpResponse) {
        return userService.deactivate(username.replace("@", ""));
    }

    @RequestMapping(value = "{username}/follow", method = RequestMethod.PATCH)
    public boolean followUser(@PathVariable String username, @RequestBody Credentials creds, HttpServletResponse response) {
        if (userService.followUser(username.replace("@", ""), creds)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
    }

    @RequestMapping(value = "{username}/unfollow", method = RequestMethod.PATCH)
    public boolean unfollowUser(@PathVariable String username, @RequestBody Credentials creds, HttpServletResponse response) {
        if (userService.unfollowUser(username.replace("@", ""), creds)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
    }

    //returns list of tweets of followed users in reverse chronological order (newest first)
    @RequestMapping(value = "{username}/feed", method = RequestMethod.PATCH)
    public List<TweetDtoOutput> feedOfTweets(@PathVariable String username, HttpServletResponse response) {
        List<TweetDtoOutput> output = userService.getFeed(username.replace("@", ""));
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = "{username}/tweets", method = RequestMethod.GET)
    public List<TweetDtoOutput> usersTweets(@PathVariable String username, HttpServletResponse response) {
        List<TweetDtoOutput> output = userService.getUsersTweets(username.replace("@", ""));
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = "{username}/mentions", method = RequestMethod.GET)
    public List<TweetDtoOutput> getMentionedInTweets(@PathVariable String username, HttpServletResponse response) {
        List<TweetDtoOutput> output = userService.getMentionedInTweets(username.replace("@", ""));
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = "{username}/followers", method = RequestMethod.GET)
    public List<UserDtoOutput> getUsersFollowers(@PathVariable String username, HttpServletResponse response) {
        List<UserDtoOutput> output = userService.getUsersFollowers(username.replace("@", ""));
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @RequestMapping(value = "{username}/following", method = RequestMethod.GET)
    public List<UserDtoOutput> getUsersLeaders(@PathVariable String username, HttpServletResponse response) {
        List<UserDtoOutput> output = userService.getUsersLeaders(username.replace("@", ""));
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
