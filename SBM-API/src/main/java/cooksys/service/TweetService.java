package cooksys.service;

import cooksys.dto.*;
import cooksys.entity.Credentials;
import cooksys.entity.Hashtag;
import cooksys.entity.Tweet;
import cooksys.entity.User;
import cooksys.mapper.HashtagMapper;
import cooksys.mapper.TweetMapper;
import cooksys.mapper.UserMapper;
import cooksys.repository.HashtagRepository;
import cooksys.repository.TweetRepository;
import cooksys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TweetMapper tweetMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private HashtagMapper hashtagMapper;

    public List<TweetDtoOutput> getAll() {
        return tweetRepository
                .findAll()
                .stream().filter(Tweet::isAlive)
                .map(tweetMapper::toTweetDtoOutput)
                .collect(Collectors.toList());
    }

    public TweetDtoOutput post(TweetDtoSimpleInput tweetDtoSimpleInput) {
        Credentials creds = tweetDtoSimpleInput.getCredentials();
        User userPostingTweet = userRepository.findByCredentialsUsernameAndCredentialsPassword(creds.getUsername(), creds.getPassword());
        checkAndActivateUser(userPostingTweet);
        if (userPostingTweet != null) {
            Tweet tweet = tweetMapper.toTweet(tweetDtoSimpleInput);
            tweet.setAuthor(userPostingTweet);
            setMentions(tweet);
            tweet.setHashtags(new ArrayList<>(saveHashtags(tweet)));
            Long id = tweetRepository.saveAndFlush(tweet).getId();
            return tweetMapper.toTweetDtoOutput(tweetRepository.findOne(id));
        }
        return null;
    }

    public TweetDtoOutput getTweet(Long id) {
        return tweetMapper.toTweetDtoOutput(tweetRepository.findOne(id));
    }

    // TODO better error catching
    public TweetDtoOutput deactivate(Long id, Credentials credentials) {
        if (userRepository.findByCredentialsUsernameAndCredentialsPassword(credentials.getUsername(), credentials.getPassword()) != null) {
            Tweet tweet = tweetRepository.findOne(id);
            tweet.setAlive(false);
            tweetRepository.saveAndFlush(tweet);
            return tweetMapper.toTweetDtoOutput(tweet);
        }
        return null;
    }

    public boolean likeTweet(Long id, Credentials credentials) {
        User user = userRepository.findByCredentialsUsernameAndCredentialsPasswordAndIsActiveTrue(credentials.getUsername(), credentials.getPassword());
        Tweet tweet = tweetRepository.findOne(id);
        if (user != null && tweet != null) {
            tweet.getLikedIt().add(user);
            tweetRepository.saveAndFlush(tweet);
            return true;
        }
        return false;
    }

    public TweetDtoOutput replyToTweet(Long idOfOriginal, TweetDtoSimpleInput tweetDtoSimpleInput) {
        Credentials creds = tweetDtoSimpleInput.getCredentials();
        User userPostingTweet = userRepository.findByCredentialsUsernameAndCredentialsPasswordAndIsActiveTrue(creds.getUsername(), creds.getPassword());
        checkAndActivateUser(userPostingTweet);
        if (userPostingTweet != null) {
            Tweet tweet = tweetMapper.toTweet(tweetDtoSimpleInput);
            tweet.setAuthor(userPostingTweet);
            setMentions(tweet);
            tweet.setOriginalTweetReply(tweetRepository.findOne(idOfOriginal));
            tweet.setHashtags(new ArrayList<>(saveHashtags(tweet)));
            Long savedTweet = tweetRepository.saveAndFlush(tweet).getId();
            return tweetMapper.toTweetDtoOutput(tweetRepository.findOne(savedTweet));
        }
        return null;
    }

    public TweetDtoOutput repostTweet(Long idOfOriginal, TweetDtoRepost tweetDtoRepost) {
        Credentials creds = tweetDtoRepost.getCredentials();
        User userPostingTweet = userRepository.findByCredentialsUsernameAndCredentialsPasswordAndIsActiveTrue(creds.getUsername(), creds.getPassword());
        checkAndActivateUser(userPostingTweet);
        if (userPostingTweet != null) {
            Tweet tweet = tweetMapper.toTweetFromRepost(tweetDtoRepost);
            tweet.setAuthor(userPostingTweet);
            tweet.setParentTweetRepost(tweetRepository.findOne(idOfOriginal));
            Long tweetId = tweetRepository.saveAndFlush(tweet).getId();
            return tweetMapper.toTweetDtoOutput(tweetRepository.findOne(tweetId));
        }
        return null;
    }

    public List<HashtagDtoOutput> getTags(Long id) {
        return tweetRepository
                .findByIdAndIsAliveTrue(id)
                .getHashtags()
                .stream()
                .map(hashtagMapper::toHashtagDtoOutput)
                .collect(Collectors.toList());
    }

    public List<UserDtoOutput> getLikes(Long id) {
        return tweetRepository
                .findByIdAndIsAliveTrue(id)
                .getLikedIt()
                .stream()
                .filter(User::isActive)
                .map(userMapper::toUserDtoOutput)
                .collect(Collectors.toList());
    }

    public List<TweetDtoOutput> getReplyTweets(Long id) {
        return tweetRepository
                .findByIdAndIsAliveTrue(id)
                .getReplies()
                .stream()
                .filter(Tweet::isAlive)
                .map(tweetMapper::toTweetDtoOutput)
                .collect(Collectors.toList());
    }

    public List<TweetDtoOutput> getRepostsOfTweet(Long id) {
        return tweetRepository.findByIdAndIsAliveTrue(id)
                .getReposts()
                .stream()
                .filter(Tweet::isAlive)
                .map(tweetMapper::toTweetDtoOutput)
                .collect(Collectors.toList());
    }

    public List<UserDtoOutput> getMentionedUsers(Long id) {
        return tweetRepository.findByIdAndIsAliveTrue(id)
                .getMentions()
                .stream()
                .filter(User::isActive)
                .map(userMapper::toUserDtoOutput)
                .collect(Collectors.toList());
    }

//    public List<TweetDtoOutput> getContextTweets(Long id) {
//        Tweet tweet = tweetRepository.findOne(id);
//        Set<Tweet> =
//        return null;
//    }

//  Utility methods------------------------------------------
    // ACTIVATE TWEET
    private void checkAndActivateUser(User user) {
        if (user != null && user.isActive() == false) {
            user.setActive(true);
            userRepository.saveAndFlush(user);
        }
    }

    // TODO offload to userService
    private Set<String> parseUsers(String content){
        Set<String> names = new HashSet<>();
        String[] words = content.split(" ");
        for (String word : words) {
            if (word.startsWith("@") && !word.substring(1).equals("")) {
                names.add(word.substring(1));
            }
        }
        return names;
    }

    // TODO offload to hashtagService or utility service or object class
    private Set<String> parseHashtags(String content) {
        Set<String> hashtags = new HashSet<>();
        Set<String> words = new HashSet<>(Arrays.asList(content.split(" ")));
        for (String word : words) {
            if (word.startsWith("#") && !word.substring(1).equals("")) {
                hashtags.add(word.substring(1));
            }
        }
        return hashtags;
    }

    private void setMentions(Tweet tweet) {
        List<User> gottenUsers = new ArrayList<>();
        for (String aUsername : parseUsers(tweet.getContent())) {
            User found = userRepository.findByCredentialsUsernameAndIsActiveTrue(aUsername);
            if (found != null) {
                gottenUsers.add(userRepository.findOne(found.getId()));
            }
        }
        tweet.setMentions(gottenUsers);
    }

    // TODO offload to hashtagService or some utility class
    private Set<Hashtag> saveHashtags(Tweet tweet) {
        Set<String> hashtagStrings = parseHashtags(tweet.getContent());
        List<String> allHashtags = hashtagRepository
                .findAll()
                .stream()
                .map(Hashtag::getLabel)
                .collect(Collectors.toList());
        for (String hashtag : hashtagStrings) {
            if (!allHashtags.contains(hashtag)) {
                hashtagRepository.saveAndFlush(new Hashtag(hashtag));
            } else {
                Hashtag hash = hashtagRepository.findByLabel(hashtag);
                hash.setLast(new Date());
                hashtagRepository.saveAndFlush(hash);
            }
        }

        // TODO offload
        Set<Hashtag> gottenHashs = new HashSet<>();
        for (String aUsername : hashtagStrings) {
            Hashtag found = hashtagRepository.findByLabel(aUsername);
            if (found != null) {
                gottenHashs.add(hashtagRepository.findOne(found.getId()));
            }
        }
        return gottenHashs;
    }
}
