package cooksys.service;

import com.google.common.collect.Lists;
import cooksys.dto.TweetDtoOutput;
import cooksys.dto.UserDtoCreate;
import cooksys.dto.UserDtoOutput;
import cooksys.entity.Credentials;
import cooksys.entity.Tweet;
import cooksys.entity.User;
import cooksys.mapper.TweetMapper;
import cooksys.mapper.UserMapper;
import cooksys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetMapper tweetMapper;

    public List<UserDtoOutput> getAllUsers() {
        return userRepository.findByIsActiveTrue()
                .stream()
                .map(userMapper::toUserDtoOutput)
                .collect(Collectors.toList());
    }

    public UserDtoOutput get(Long id) {
        return userMapper.toUserDtoOutput(userRepository.getOne(id));
    }

    public UserDtoOutput post(UserDtoCreate userDtoCreate) {
        Long idOf = userRepository.saveAndFlush(userMapper.toUser(userDtoCreate)).getId();
        return userMapper.toUserDtoOutput(userRepository.findOne(idOf));
    }

    public UserDtoOutput getUserByName(String username) {
        return userMapper.toUserDtoOutput(userRepository.findByCredentialsUsername(username));
    }

    // TODO refactor to get rid of this if statement hell
    public UserDtoOutput patch(String username, UserDtoCreate userDto) {
        User userExistant = userRepository.findByCredentialsUsername(username);

        if (userExistant == null
                || !userExistant.getCredentials().getUsername().equals(userDto.getCredentials().getUsername())
                || !userExistant.getCredentials().getPassword().equals(userDto.getCredentials().getPassword())) {
            return null;
        } else {
            if (userDto.getProfile().getEmail() != null) {
                userExistant.getProfile().setEmail(userDto.getProfile().getEmail());
            }
            if (userDto.getProfile().getFirstName() != null) {
                userExistant.getProfile().setFirstName(userDto.getProfile().getFirstName());
            }
            if (userDto.getProfile().getLastName() != null) {
                userExistant.getProfile().setFirstName(userDto.getProfile().getLastName());
            }
            if(userDto.getProfile().getPhone() != null) {
                userExistant.getProfile().setPhone(userDto.getProfile().getPhone());
            }
            userRepository.saveAndFlush(userExistant);
            return userMapper.toUserDtoOutput(userExistant);
        }
    }

    public UserDtoOutput deactivate(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        if (user != null) {
            user.setActive(false);
            userRepository.saveAndFlush(user);
            return userMapper.toUserDtoOutput(user);
        } else {
            return null;
        }
    }

    public boolean followUser(String username, Credentials creds) {
        User follower = userRepository.findByCredentialsUsernameAndCredentialsPasswordAndIsActiveTrue(creds.getUsername(), creds.getPassword());
        User leader = userRepository.findByCredentialsUsernameAndIsActiveTrue(username);
        if (leader != null && follower != null && !leader.getFollowers().contains(follower)) {
            follower.getLeaders().add(leader);
            userRepository.saveAndFlush(follower);
            return true;
        }
        return false;
    }

    public boolean unfollowUser(String username, Credentials creds) {
        User follower = userRepository.findByCredentialsUsernameAndCredentialsPasswordAndIsActiveTrue(creds.getUsername(), creds.getPassword());
        User leader = userRepository.findByCredentialsUsernameAndIsActiveTrue(username);
        if (leader != null && follower != null && leader.getFollowers().contains(follower)) {
            follower.getLeaders().remove(leader);
            userRepository.saveAndFlush(follower);
            return true;
        }
        return false;
    }

    public List<TweetDtoOutput> getFeed(String username) {
        User user = userRepository.findByCredentialsUsernameAndIsActiveTrue(username);
        List<Tweet> outputTweets = user.getUsersTweets();
        for (User leader : user.getLeaders()) {
            outputTweets.addAll(leader.getUsersTweets());
        }
        return Lists.reverse(outputTweets
                .stream()
                .filter(Tweet::isAlive)
                .map(tweetMapper::toTweetDtoOutput)
                .sorted(Comparator.comparing(TweetDtoOutput::getPosted))
                .collect(Collectors.toList()));
    }

    public List<TweetDtoOutput> getUsersTweets(String username) {
        User user = userRepository.findByCredentialsUsernameAndIsActiveTrue(username);
        List<Tweet> usersTweets = user.getUsersTweets();
        return Lists.reverse(usersTweets
                .stream()
                .filter(Tweet::isAlive)
                .map(tweetMapper::toTweetDtoOutput)
                .sorted(Comparator.comparing(TweetDtoOutput::getPosted))
                .collect(Collectors.toList()));
    }

    public List<TweetDtoOutput> getMentionedInTweets(String username) {
        return userRepository
                .findByCredentialsUsernameAndIsActiveTrue(username)
                .getMentionedIn()
                .stream()
                .filter(Tweet::isAlive)
                .map(tweetMapper::toTweetDtoOutput)
                .collect(Collectors.toList());
    }

    public List<UserDtoOutput> getUsersFollowers(String username) {
        return userRepository
                .findByCredentialsUsernameAndIsActiveTrue(username)
                .getFollowers()
                .stream()
                .filter(User::isActive)
                .map(userMapper::toUserDtoOutput)
                .collect(Collectors.toList());
    }

    public List<UserDtoOutput> getUsersLeaders(String username) {
        return userRepository
                .findByCredentialsUsernameAndIsActiveTrue(username)
                .getLeaders()
                .stream()
                .filter(User::isActive)
                .map(userMapper::toUserDtoOutput)
                .collect(Collectors.toList());
    }
}
