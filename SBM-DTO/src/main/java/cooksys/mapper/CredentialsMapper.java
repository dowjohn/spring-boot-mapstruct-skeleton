package cooksys.mapper;

import cooksys.dto.CredentialsDto;
import cooksys.entity.Credentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
    Credentials toCredentials(CredentialsDto credentialsDto);
}
