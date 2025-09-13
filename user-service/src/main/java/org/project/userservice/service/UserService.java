package org.project.userservice.service;

import org.project.userservice.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findUserByEmail(String email);

    UserDto save(UserDto userDto);

    UserDto updateUserByEmail(String email, UserDto userDto);

    void deleteUserByEmail(String email);
}
