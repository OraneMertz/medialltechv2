package com.biblio.medialltech.service;

import com.biblio.medialltech.dto.UserDTO;
import com.biblio.medialltech.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    boolean authenticateUser(String username, String password);
    User createUser(UserDTO userDTO);
    User updateUser(Long id, UserDTO userDTO);
    boolean deleteUser(Long id);
    boolean isUsernameExists(String username);
    boolean isEmailExists(String email);
}
