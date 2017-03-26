package cooksys.service;

import com.google.common.collect.Lists;
import cooksys.dto.TweetDtoOutput;
import cooksys.dto.UserDtoCreate;
import cooksys.dto.UserDtoOutput;
import cooksys.entity.Credentials;
import cooksys.entity.Profile;
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

    // TODO refactor to get rid of if statement hell
    public UserDtoOutput patch(String username, UserDtoCreate userDto) {
        User userExistant = userRepository.findByCredentialsUsername(username);
        Credentials existingCreds = userExistant.getCredentials();
        Credentials dtoCreds = userDto.getCredentials();
        if (userExistant == null
                || !existingCreds.getUsername().equals(dtoCreds.getUsername())
                || !existingCreds.getPassword().equals(dtoCreds.getPassword())) {
            return null;
        } else {
            Profile existingProfile = userExistant.getProfile();
            Profile dtoProfile = userDto.getProfile();
            if (userDto.getProfile().getEmail() != null) {
                existingProfile.setEmail(dtoProfile.getEmail());
            }
            if (userDto.getProfile().getFirstName() != null) {
                existingProfile.setFirstName(dtoProfile.getFirstName());
            }
            if (userDto.getProfile().getLastName() != null) {
                existingProfile.setFirstName(dtoProfile.getLastName());
            }
            if(userDto.getProfile().getPhone() != null) {
                existingProfile.setPhone(dtoProfile.getPhone());
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
