package com.biblio.medialltech.service;

import com.biblio.medialltech.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    User createUser(User user);

    User updateUser(User user);

    boolean deleteUser(Long id);

    boolean authenticateUser(String username, String password);

    boolean isUsernameExists(String username);

    boolean isEmailExists(String email);
}
