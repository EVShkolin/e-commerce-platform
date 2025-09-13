package org.project.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.project.userservice.dto.UserDto;
import org.project.userservice.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "registrationDate", ignore = true)
    User toEntity(UserDto userDto);

    List<UserDto> toDto(List<User> users);
}
