package cooksys.mapper;

import cooksys.dto.HashtagDtoOutput;
import cooksys.entity.Hashtag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    @Mappings({
            @Mapping(source = "label",target = "label"),
            @Mapping(source = "first", target = "first"),
            @Mapping(source = "last", target = "last")
    })
    HashtagDtoOutput toHashtagDtoOutput(Hashtag hashtag);

//    Hashtag toHashtag(HashtagDtoOutput hashtag);
}
