package cooksys.service;

import cooksys.entity.User;
import cooksys.repository.HashtagRepository;
import cooksys.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by john on 3/24/17.
 */
@Service
public class ValidationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    public boolean userAvailable(String substring) {
        return userRepository.findByCredentialsUsername(substring) != null;
    }

    // Todo fix findBy to include check of isActive
    public boolean userExists(String substring) {
        User usable = userRepository.findByCredentialsUsername(substring);
        if (usable != null && usable.isActive()) {
            return true;
        }
        return false;
    }

    public boolean tagExists(String substring) {
        return hashtagRepository.findByLabel(substring) != null;
    }
}
