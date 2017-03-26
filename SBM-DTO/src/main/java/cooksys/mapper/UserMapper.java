package cooksys.mapper;

import cooksys.dto.UserDtoCreate;
import cooksys.dto.UserDtoOutput;
import cooksys.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
            @Mapping(target="timestamp", ignore=true)
    })
    User toUser(UserDtoCreate userDtoCreate);

    @Mappings({
            @Mapping(source = "credentials.username",target="username"),
            @Mapping(source = "profile", target = "profile"),
            @Mapping(source = "timestamp", target = "timestamp")
    })
    UserDtoOutput toUserDtoOutput(User user);
}
