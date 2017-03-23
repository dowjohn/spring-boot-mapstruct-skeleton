package cooksys.service;

import cooksys.dto.TweetDtoOutput;
import cooksys.dto.TweetDtoSimpleInput;
import cooksys.entity.Hashtag;
import cooksys.entity.Tweet;
import cooksys.entity.User;
import cooksys.mapper.TweetMapper;
import cooksys.repository.HashtagRepository;
import cooksys.repository.TweetRepository;
import cooksys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private HashtagRepository hashtagRepository;

    public List<TweetDtoOutput> getAll() {
        return tweetRepository.findAll().stream().map(tweetMapper::toTweetDtoOutput).collect(Collectors.toList());
    }

    public TweetDtoOutput post(TweetDtoSimpleInput tweetDtoSimpleInput) {
//        tweetRepository.save(tweetMapper.toTweet(tweetDtoSimpleInput));
        User userPostingTweet = userRepository.findByCredentialsUsername(tweetDtoSimpleInput.getCredentials().getUsername());

        if (userPostingTweet != null && userPostingTweet.getCredentials().getPassword().equals(tweetDtoSimpleInput.getCredentials().getPassword())) {
            Tweet tweet = tweetMapper.toTweet(tweetDtoSimpleInput);
            tweet.setContent(tweetDtoSimpleInput.getContent());
            tweet.setAuthor(userPostingTweet);
            Long tweetId = tweetRepository.save(tweet).getId();
            System.out.println(tweet.getId());
//            Tweet tweety = tweetRepository.findOne(tweetId);

//            saveHashtags(tweety);
//
//            List<User> gottenUsers = new ArrayList<>();
//            for (String aUsername : parseUsers(tweety.getContent())) {
//                User found = userRepository.findByCredentialsUsername(aUsername);
//                if (found != null) {
//                    gottenUsers.add(userRepository.findOne(found.getId()));
//                }
//            }
//            tweety.setMentions(gottenUsers);
//            tweetRepository.saveAndFlush(tweety);
            TweetDtoOutput out =  tweetMapper.toTweetDtoOutput(tweetRepository.findOne(tweetId));
            return out;
        }
        return null;
    }

    public void saveHashtags(Tweet tweet) {
        Set<String> hashtagsInString = parseHashtags(tweet.getContent());
        Set<Hashtag> hashtags = new HashSet<>();
        for (String hashtagString : hashtagsInString) {
            hashtags.add(new Hashtag(hashtagString));
        }
        hashtagRepository.save(hashtags);
        hashtagRepository.flush();
    }

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

    public Set<String> parseHashtags(String content) {
        Set<String> hashtags = new HashSet<>();
        String[] words = content.split(" ");
        for (String word : words) {
            if (word.startsWith("#")) {
                hashtags.add(word.substring(1));
            }
        }
        return hashtags;
    }
}
