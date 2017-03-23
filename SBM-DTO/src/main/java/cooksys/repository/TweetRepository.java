package cooksys.repository;

import cooksys.entity.Hashtag;
import cooksys.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
//    Hashtag findByHashtagsLabel(String label);
}
