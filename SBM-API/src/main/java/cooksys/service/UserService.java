package cooksys.service;

import cooksys.dto.UserDtoCreate;
import cooksys.dto.UserDtoOutput;
import cooksys.entity.Credentials;
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

    public UserDtoOutput post(UserDtoCreate userDtoCreate) {
        Long idOf = userRepository.saveAndFlush(userMapper.toUser(userDtoCreate)).getId();
        // TODO Timestamp is returning null;
        return userMapper.toUserDtoOutput(userRepository.findOne(idOf));

    }

    public boolean userAvailable(String substring) {
        if (userRepository.findByCredentialsUsername(substring) != null) {
            return true;
        }
        return false;
    }

    public boolean userExists(String substring) {
        User usable = userRepository.findByCredentialsUsername(substring);
        if (usable != null && usable.isActive()) {
            return true;
        }
        return false;
    }

    public UserDtoOutput getUserByName(String username) {
        return userMapper.toUserDtoOutput(userRepository.findByCredentialsUsername(username));
    }

    public UserDtoOutput patch(String username, UserDtoCreate userDto) {
        User userExistant = userRepository.findByCredentialsUsername(username);

        if (userExistant == null
                || !userExistant.getCredentials().getUsername().equals(userDto.getCredentials().getUsername())
                || !userExistant.getCredentials().getPassword().equals(userDto.getCredentials().getPassword())) {
            return null;
        } else {
//            copyNonNullProperties(userMapper.toUser(userDto), userExistant);
//            userExistant.setProfile(userDto.getProfile());
            if (userDto.getProfile().getEmail() != null) {
                userExistant.getProfile().setEmail(userDto.getProfile().getEmail());
            }
            if (userDto.getProfile().getFirstName() != null) {
                userExistant.getProfile().setFirstName(userDto.getProfile().getFirstName());
            }
            if (userDto.getProfile().getLastName() != null) {
                userExistant.getProfile().setFirstName(userDto.getProfile().getLastName());
            }
            if(userDto.getProfile().getPhone() != null) {
                userExistant.getProfile().setPhone(userDto.getProfile().getPhone());
            }
            userRepository.saveAndFlush(userExistant);
            return userMapper.toUserDtoOutput(userExistant);
        }
    }

    public UserDtoOutput deactivate(String username) {
        User user = userRepository.findByCredentialsUsername(username);
        user.setActive(false);
        userRepository.save(user);
        return userMapper.toUserDtoOutput(user);
    }

    public boolean followUser(String username, Credentials creds) {
        User leader = userRepository.findByCredentialsUsername(creds.getUsername());
        User follower = userRepository.findByCredentialsUsername(username);
        if (leader != null && follower != null && !leader.getFollowers().contains(follower)) {
            leader.getFollowers().add(follower);
            userRepository.saveAndFlush(leader);
            return true;
        }
        return false;
    }

//    public static void copyNonNullProperties(Object src, Object target) {
//        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
//    }
//
//    public static String[] getNullPropertyNames (Object source) {
//        final BeanWrapper src = new BeanWrapperImpl(source);
//        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
//
//        Set<String> emptyNames = new HashSet<String>();
//        for(java.beans.PropertyDescriptor pd : pds) {
//            Object srcValue = src.getPropertyValue(pd.getName());
//            if (srcValue == null) emptyNames.add(pd.getName());
//        }
//        String[] result = new String[emptyNames.size()];
//        return emptyNames.toArray(result);
//    }
}
