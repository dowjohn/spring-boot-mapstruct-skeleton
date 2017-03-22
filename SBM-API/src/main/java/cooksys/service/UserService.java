package cooksys.service;

import cooksys.dto.UserDto;
import cooksys.dto.UserDtoOutput;
import cooksys.entity.User;
import cooksys.mapper.UserMapper;
import cooksys.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserMapper userMapper;
    private UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public List<UserDtoOutput> index() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toUserDtoOutput)
                .collect(Collectors.toList());
    }


    public UserDtoOutput get(Long id) {
        return userMapper.toUserDtoOutput(userRepository.getOne(id));
    }

    public Long post(UserDto userDto) {
        return userRepository.save(userMapper.toUser(userDto)).getId();
    }
}
