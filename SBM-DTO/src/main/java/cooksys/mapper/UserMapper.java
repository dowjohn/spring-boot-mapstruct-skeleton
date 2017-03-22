package cooksys.mapper;

import cooksys.dto.UserDto;
import cooksys.dto.UserDtoOutput;
import cooksys.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring"
//        ,
//        uses = {ReferenceMapper.class, CredentialsMapper.class}
        )
public interface UserMapper {
    @Mappings({
//            @Mapping(target="credentials", ignore=true),
            @Mapping(target="timestamp", ignore=true)
    })
    User toUser(UserDto userDto);


    @Mappings({
            @Mapping(target="credentials", ignore=true),
//            @Mapping(target="", ignore=true)
    })
    UserDto toUserDto(User user);

    @Mappings({
            @Mapping(source = "credentials.username",target="username"),
            @Mapping(source = "profile", target = "profile"),
            @Mapping(source = "timestamp", target = "timestamp")
    })
    UserDtoOutput toUserDtoOutput(User user);
}
