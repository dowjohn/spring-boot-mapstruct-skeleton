package cooksys.service;

import cooksys.dto.HashtagDtoOutput;
import cooksys.dto.TweetDtoOutput;
import cooksys.entity.Tweet;
import cooksys.mapper.HashtagMapper;
import cooksys.mapper.TweetMapper;
import cooksys.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HashtagService {

    @Autowired
    HashtagRepository hashtagRepository;

    @Autowired
    HashtagMapper hashtagMapper;

    @Autowired
    TweetMapper tweetMapper;

    // TODO currently returning milliseconds instead of datetime, this is true for all timestamps currently
    public List<HashtagDtoOutput> getAll() {
        return hashtagRepository
                .findAll()
                .stream()
                .map(hashtagMapper::toHashtagDtoOutput)
                .collect(Collectors.toList());
    }

    // TODO add null check and error handling, untested
    public List<TweetDtoOutput> getTweetsByHashtag(String label) {
        return hashtagRepository
                .findByLabel(label)
                .getTweetsWithHashtag()
                .stream()
                .filter(Tweet::isAlive)
                .map(tweetMapper::toTweetDtoOutput)
                .collect(Collectors.toList());
    }
}
