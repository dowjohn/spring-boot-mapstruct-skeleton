package cooksys.repository;

import cooksys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by student-2 on 3/21/2017.
 */
public interface UserRepository extends JpaRepository<User, Long>{
    User findByCredentialsUsername(String userName);
}
