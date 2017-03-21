package cooksys.mapper;

import cooksys.dto.UserDto;
import cooksys.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User toUser(UserDto userDto);

    UserDto toUserDto(User user);
}
