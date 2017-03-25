package cooksys.repository;

import cooksys.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    Tweet findByIdAndIsAliveTrue(Long id);
}
