package org.project.userservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.project.userservice.dto.UserDto;
import org.project.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable @Email String email) {
        return new ResponseEntity<>(userService.findUserByEmail(email), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.save(userDto), HttpStatus.CREATED);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserDto> updateUser(@PathVariable @Email String email, @RequestBody @Valid UserDto userDto) {
        return new ResponseEntity<>(userService.updateUserByEmail(email, userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Email String email) {
        userService.deleteUserByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
