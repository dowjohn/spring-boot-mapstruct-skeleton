package cooksys.repository;

import cooksys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByCredentialsUsername(String username);
//    List<User> findBy
}
