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
        return tweetRepository.findAll().stream().filter(x -> x.isAlive()).map(tweetMapper::toTweetDtoOutput).collect(Collectors.toList());
    }

    public TweetDtoOutput post(TweetDtoSimpleInput tweetDtoSimpleInput) {
//        tweetRepository.save(tweetMapper.toTweet(tweetDtoSimpleInput));
        User userPostingTweet = userRepository.findByCredentialsUsername(tweetDtoSimpleInput.getCredentials().getUsername());

        if (userPostingTweet != null && userPostingTweet.getCredentials().getPassword().equals(tweetDtoSimpleInput.getCredentials().getPassword())) {

            // saving tweet TODO alter mapper to do this save logic.
            Tweet tweet = tweetMapper.toTweet(tweetDtoSimpleInput);
            tweet.setContent(tweetDtoSimpleInput.getContent());
            tweet.setAuthor(userPostingTweet);
            Long tweetId = tweetRepository.saveAndFlush(tweet).getId();
            Tweet tweety = tweetRepository.findOne(tweetId);

            //Saving mentions
            List<User> gottenUsers = new ArrayList<>();
            for (String aUsername : parseUsers(tweety.getContent())) {
                User found = userRepository.findByCredentialsUsername(aUsername);
                if (found != null) {
                    gottenUsers.add(userRepository.findOne(found.getId()));
                }
            }
            tweety.setMentions(gottenUsers);
            Long yetAnotherId = tweetRepository.saveAndFlush(tweety).getId();
            TweetDtoOutput out =  tweetMapper.toTweetDtoOutput(tweety);

            // Saving hashtags
            Set<Hashtag> saved = saveHashtags(tweety);

            // Saving tweets hashtags to tweet (relation) TODO alter to use set in entity and these nested methods.
            Tweet bestTweetEver = tweetRepository.getOne(yetAnotherId);
            List<Hashtag> dumbyList = new ArrayList<>();
            dumbyList.addAll(saved);
            bestTweetEver.setHashtags(dumbyList);
            tweetRepository.saveAndFlush(bestTweetEver);
            return out;
        }
        return null;
    }

    // TODO offload to hashtagService or some utility class
    public Set<Hashtag> saveHashtags(Tweet tweet) {
        Set<String> hashtagStrings = parseHashtags(tweet.getContent());
        List<String> allHashtags = hashtagRepository.findAll().stream().map(x -> x.getLabel()).collect(Collectors.toList());
        for (String hashtag : hashtagStrings) {
            if (!allHashtags.contains(hashtag)) {
                System.out.println(hashtag);
                hashtagRepository.saveAndFlush(new Hashtag(hashtag));
            }
        }

        // TODO offload
        Set<Hashtag> gottenHashs = new HashSet<>();
        for (String aUsername : hashtagStrings) {
            Hashtag found = hashtagRepository.findByLabel(aUsername);
            if (found != null) {
                System.out.println(found.getLabel() + "offload");
                gottenHashs.add(hashtagRepository.findOne(found.getId()));
            }
        }
        return gottenHashs;
    }

    // TODO offload to userService
    public Set<String> parseUsers(String content){
        Set<String> names = new HashSet<>();
        String[] words = content.split(" ");
        for (String word : words) {
            if (word.startsWith("@")) {
                names.add(word.substring(1));
            }
        }
        return names;
    }

    // TODO offload to hashtagService
    public Set<String> parseHashtags(String content) {
        Set<String> hashtags = new HashSet<>();
        String[] words = content.split(" ");
        for (String word : words) {
            if (word.startsWith("#")) {
                hashtags.add(word.substring(1));
                System.out.println(word);
            }
        }
        return hashtags;
    }

    // TODO null check if repository cant find tweet of Long id
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

    // TODO better error catching than returning null
    public boolean likeTweet(Long id, Credentials credentials) {
        User user = userRepository.findByCredentialsUsernameAndCredentialsPassword(credentials.getUsername(), credentials.getPassword());
        Tweet tweet = tweetRepository.findOne(id);
        if (user != null && tweet != null) {
//            user.getLikedTweets().add(tweet);
//            userRepository.saveAndFlush(user);
            tweet.getLikedIt().add(user);
            tweetRepository.saveAndFlush(tweet);
            return true;
        }
        return false;
    }

    // TODO add error catching
    public TweetDtoOutput replyToTweet(Long id, TweetDtoSimpleInput tweetDtoSimpleInput) {
        User userPostingTweet = userRepository.findByCredentialsUsername(tweetDtoSimpleInput.getCredentials().getUsername());

        if (userPostingTweet != null && userPostingTweet.getCredentials().getPassword().equals(tweetDtoSimpleInput.getCredentials().getPassword())) {

            // saving tweet TODO alter mapper to do this save logic.
            Tweet tweet = tweetMapper.toTweet(tweetDtoSimpleInput);
            tweet.setContent(tweetDtoSimpleInput.getContent());
            tweet.setAuthor(userPostingTweet);

            //-----------------------------------------reply logic

            Tweet repliedTo = tweetRepository.findOne(id);
            tweet.setOriginalTweetReply(repliedTo);
//            tweetRepository.save(repliedTo);
            //-----------------------------------------
            Long tweetId = tweetRepository.saveAndFlush(tweet).getId();
//            tweetRepository.save(repliedTo);
            Tweet tweety = tweetRepository.findOne(tweetId);

            //Saving mentions
            List<User> gottenUsers = new ArrayList<>();
            for (String aUsername : parseUsers(tweety.getContent())) {
                User found = userRepository.findByCredentialsUsername(aUsername);
                if (found != null) {
                    gottenUsers.add(userRepository.findOne(found.getId()));
                }
            }
            tweety.setMentions(gottenUsers);
            Long yetAnotherId = tweetRepository.saveAndFlush(tweety).getId();
            TweetDtoOutput out =  tweetMapper.toTweetDtoOutput(tweety);

            // Saving hashtags
            Set<Hashtag> saved = saveHashtags(tweety);

            // Saving tweets hashtags to tweet (relation) TODO alter to use set in entity and these nested methods.
            Tweet bestTweetEver = tweetRepository.getOne(yetAnotherId);
            List<Hashtag> dumbyList = new ArrayList<>();
            dumbyList.addAll(saved);
            bestTweetEver.setHashtags(dumbyList);
            tweetRepository.saveAndFlush(bestTweetEver);
            return out;
        }
        return null;
    }

    // TODO add error catching
    public TweetDtoOutput repostTweet(Long id, TweetDtoRepost tweetDtoRepost) {
        User userPostingTweet = userRepository.findByCredentialsUsername(tweetDtoRepost.getCredentials().getUsername());

        if (userPostingTweet != null && userPostingTweet.getCredentials().getPassword().equals(tweetDtoRepost.getCredentials().getPassword())) {

            // saving tweet TODO alter mapper to do this save logic.
            Tweet tweet = tweetMapper.toTweetFromRepost(tweetDtoRepost);
            tweet.setAuthor(userPostingTweet);

            //-----------------------------------------reply logic

            Tweet repostOf = tweetRepository.findOne(id);
            tweet.setParentTweetRepost(repostOf);
//            tweetRepository.save(repliedTo);
            //-----------------------------------------
            Long tweetId = tweetRepository.saveAndFlush(tweet).getId();
            Tweet tweety = tweetRepository.findOne(tweetId);

            //Saving mentions
//            List<User> gottenUsers = new ArrayList<>();
//            for (String aUsername : parseUsers(tweety.getContent())) {
//                User found = userRepository.findByCredentialsUsername(aUsername);
//                if (found != null) {
//                    gottenUsers.add(userRepository.findOne(found.getId()));
//                }
//            }
//            tweety.setMentions(gottenUsers);
//            Long yetAnotherId = tweetRepository.saveAndFlush(tweety).getId();
            TweetDtoOutput out =  tweetMapper.toTweetDtoOutput(tweety);

            // Saving hashtags
//            Set<Hashtag> saved = saveHashtags(tweety);

            // Saving tweets hashtags to tweet (relation) TODO alter to use set in entity and these nested methods.
//            Tweet bestTweetEver = tweetRepository.getOne(yetAnotherId);
//            List<Hashtag> dumbyList = new ArrayList<>();
//            dumbyList.addAll(saved);
//            bestTweetEver.setHashtags(dumbyList);
//            tweetRepository.saveAndFlush(bestTweetEver);
            return out;
        }
        return null;
    }

    // TODO add error catching
    public List<HashtagDtoOutput> getTags(Long id) {
        return tweetRepository.findOne(id).getHashtags().stream().map(hashtagMapper::toHashtagDtoOutput).collect(Collectors.toList());
    }

    // TODO add error catching. check if original tweet is alive
    public List<UserDtoOutput> getLikes(Long id) {
        return tweetRepository
                .getOne(id)
                .getLikedIt()
                .stream()
                .filter(x -> x.isActive())
                .map(userMapper::toUserDtoOutput)
                .collect(Collectors.toList());
    }

    // TODO add error handling and null catch check if orignal tweet is alive
    public List<TweetDtoOutput> getReplyTweets(Long id) {
        return tweetRepository
                .getOne(id)
                .getReplies()
                .stream()
                .filter(Tweet::isAlive)
                .map(tweetMapper::toTweetDtoOutput)
                .collect(Collectors.toList());
    }

    // TODO add error handling and null catch, check if original tweet is alive
    public List<TweetDtoOutput> getRepostsOfTweet(Long id) {
        return tweetRepository
                .getOne(id)
                .getReposts()
                .stream()
                .filter(Tweet::isAlive)
                .map(tweetMapper::toTweetDtoOutput)
                .collect(Collectors.toList());
    }

    // TODO test to see if working
    public List<UserDtoOutput> getMentionedUsers(Long id) {
        Tweet tweet =  tweetRepository.getOne(id);
        if (tweet.isAlive()) {
            return tweet.getMentions()
                    .stream()
                    .filter(x -> x.isActive())
                    .map(userMapper::toUserDtoOutput)
                    .collect(Collectors.toList());
        }
        return null;
    }

//    public List<TweetDtoOutput> getContextTweets(Long id) {
//        Tweet tweet = tweetRepository.findOne(id);
//        Set<Tweet> =
//        return null;
//    }
//    private Set<Tweet> getParent(Tweet tweet) {
//        Set<Tweet> tweets = new HashSet<>();
//        if (tweet.getParentTweetRepost() == null) {
//            return tweets;
//        } else {
//            Tweet tweety = tweet.getParentTweetRepost();
//            tweets.add(tweety);
//            tweets.addAll(getParent(tweety));
//        }
//    }

}
