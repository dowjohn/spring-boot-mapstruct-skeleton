package cooksys.controller;

import cooksys.dto.TweetDtoOutput;
import cooksys.dto.UserDtoCreate;
import cooksys.dto.UserDtoOutput;
import cooksys.entity.Credentials;
import cooksys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("users")
@Api(tags = {"public", "users"})
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping
    @ApiOperation(value = "", nickname = "getAllUsers")
    public List<UserDtoOutput> getAllUsers(HttpServletResponse response) {
        List<UserDtoOutput> output =  userService.getAllUsers();
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("@{username}")
    @ApiOperation(value = "", nickname = "getUserByName")
    public UserDtoOutput getUserByName(@RequestParam(value="username") String username, HttpServletResponse response) {
        UserDtoOutput output = userService.getUserByName(username);
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping
    @ApiOperation(value = "", nickname = "createUser")
    public UserDtoOutput post(@RequestBody UserDtoCreate userDtoCreate, HttpServletResponse httpResponse) {
        UserDtoOutput output = userService.post(userDtoCreate);
        if (output != null) {
            httpResponse.setStatus(HttpServletResponse.SC_CREATED);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PatchMapping("@{username}")
    @ApiOperation(value = "", nickname = "updateUser")
    public UserDtoOutput patch(@PathVariable String username, @RequestBody UserDtoCreate userDto, HttpServletResponse httpResponse) {
        UserDtoOutput output = userService.patch(username, userDto);
        if (output != null) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("@{username}")
    @ApiOperation(value = "", nickname = "deactivateUser")
    public UserDtoOutput delete(@PathVariable String username, HttpServletResponse httpResponse) {
        UserDtoOutput output = userService.deactivate(username);
        if (output != null) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "@{username}/follow", method = RequestMethod.PATCH)
    public void followUser(@PathVariable String username, @RequestBody Credentials creds, HttpServletResponse response) {
        if (userService.followUser(username, creds)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "@{username}/unfollow", method = RequestMethod.PATCH)
    public void unfollowUser(@PathVariable String username, @RequestBody Credentials creds, HttpServletResponse response) {
        if (userService.unfollowUser(username, creds)) {
            response.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        }
    }

    //returns list of tweets of followed users in reverse chronological order (newest first)
    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "@{username}/feed", method = RequestMethod.GET)
    public List<TweetDtoOutput> feedOfTweets(@PathVariable String username, HttpServletResponse response) {
        List<TweetDtoOutput> output = userService.getFeed(username);
        if (output != null && !output.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_OK);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "@{username}/tweets", method = RequestMethod.GET)
    public List<TweetDtoOutput> usersTweets(@PathVariable String username, HttpServletResponse response) {
        List<TweetDtoOutput> output = userService.getUsersTweets(username);
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "@{username}/mentions", method = RequestMethod.GET)
    public List<TweetDtoOutput> getMentionedInTweets(@PathVariable String username, HttpServletResponse response) {
        List<TweetDtoOutput> output = userService.getMentionedInTweets(username);
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "@{username}/followers", method = RequestMethod.GET)
    public List<UserDtoOutput> getUsersFollowers(@PathVariable String username, HttpServletResponse response) {
        List<UserDtoOutput> output = userService.getUsersFollowers(username);
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "@{username}/following", method = RequestMethod.GET)
    public List<UserDtoOutput> getUsersLeaders(@PathVariable String username, HttpServletResponse response) {
        List<UserDtoOutput> output = userService.getUsersLeaders(username);
        if (output != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            return output;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
