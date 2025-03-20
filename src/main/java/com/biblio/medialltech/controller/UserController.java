package com.biblio.medialltech.controller;

import com.biblio.medialltech.dto.UserDTO;
import com.biblio.medialltech.entity.User;
import com.biblio.medialltech.mapper.UserMapper;
import com.biblio.medialltech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(userMapper.toDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        if (userService.isUsernameExists(userDTO.getUsername())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (userService.isEmailExists(userDTO.getEmail())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User newUser = userService.createUser(userDTO);
        return new ResponseEntity<>(userMapper.toDTO(newUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            userDTO.setId(id);
            User updatedUser = userService.updateUser(id, userDTO);
            return new ResponseEntity<>(userMapper.toDTO(updatedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}