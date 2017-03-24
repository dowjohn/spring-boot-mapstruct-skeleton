package cooksys.repository;

import cooksys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByCredentialsUsername(String username);
    User findByCredentialsPassword(String password);
    User findByCredentialsUsernameAndIsActiveTrue(String username);
    User findByCredentialsUsernameAndCredentialsPassword(String username, String password);
    User findByCredentialsUsernameAndCredentialsPasswordAndIsActiveTrue(String username, String password);
    List<User> findByIsActiveTrue();
}
