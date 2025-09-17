package org.project.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.userservice.dto.UserDto;
import org.project.userservice.exceptionhandler.exception.UserNotFoundException;
import org.project.userservice.mapper.UserMapper;
import org.project.userservice.model.User;
import org.project.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAll() {
        log.debug("IN UserServiceImpl find all users");

        return userMapper.toDto(userRepository.findAll());
    }

    @Override
    public UserDto findUserByEmail(String email) {
        log.debug("IN UserServiceImpl find user {}",email);

        return userRepository.findUserByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public UserDto save(UserDto userDto) {
        log.info("IN UserServiceImpl save user {}", userDto);

        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUserByEmail(String email, UserDto userDto) {
        log.info("User with email {} updated username to {}", email, userDto.getUsername());

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        user.setUsername(userDto.getUsername());

        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUserByEmail(String email) {
        log.info("IN UserServiceImpl delete user {}", email);

        if (!userRepository.existsByEmail(email))
            throw new UserNotFoundException(email);

        userRepository.deleteUserByEmail(email);
    }
}
