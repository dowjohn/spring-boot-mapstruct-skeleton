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
    private HashtagRepository hashtagRepository;

    public List<TweetDtoOutput> getAll() {
        return tweetRepository.findAll().stream().map(tweetMapper::toTweetDtoOutput).collect(Collectors.toList());
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

    // TODO offload to hashtagService
    public Set<Hashtag> saveHashtags(Tweet tweet) {
        Set<String> hashtagStrings = parseHashtags(tweet.getContent());
        Set<Hashtag> hashtags = new HashSet<>();
        for (String hashtag : hashtagStrings) {
            hashtags.add(new Hashtag(hashtag));
        }
        hashtagRepository.save(hashtags);
        hashtagRepository.flush();

        Set<Hashtag> gottenHashs = new HashSet<>();
        for (String aUsername : hashtagStrings) {
            Hashtag found = hashtagRepository.findByLabel(aUsername);
            if (found != null) {
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
