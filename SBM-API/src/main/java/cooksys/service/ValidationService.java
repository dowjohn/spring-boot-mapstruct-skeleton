package cooksys.service;

import cooksys.dto.CredentialsDto;
import cooksys.entity.Credentials;
import cooksys.mapper.CredentialsMapper;
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

    @Autowired
    private CredentialsMapper credentialsMapper;

    public boolean userAvailable(String substring) {
        return userRepository.findByCredentialsUsername(substring) != null;
    }

    public boolean userExists(String substring) {
        return userRepository.findByCredentialsUsernameAndIsActiveTrue(substring) != null;
    }

    public boolean tagExists(String substring) {
        return hashtagRepository.findByLabel(substring) != null;
    }

    public boolean login(CredentialsDto creds) {
        Credentials credentials = credentialsMapper.toCredentials(creds);
        return userRepository.findByCredentialsUsernameAndCredentialsPassword(credentials.getUsername(), credentials.getPassword()) != null;
    }
}
