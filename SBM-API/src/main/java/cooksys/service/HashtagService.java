package cooksys.service;

import cooksys.entity.Hashtag;
import cooksys.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HashtagService {

    @Autowired
    private TweetRepository tweetRepository;

    public boolean tagExists(String substring) {
        Hashtag hashTag = tweetRepository.findByHashtagsLabel(substring);
        if (hashTag != null) {
            return true;
        }
        return false;
    }
}
