package com.biblio.medialltechv2.service;

import com.biblio.medialltechv2.model.Users;

import java.util.List;

public interface UsersService {

    Users getUserByUsername(String username);

    List<Users> getUsers();

    Users getUserById(String id);

    void addUser(Users user);

    void updateUser(Users user);

    void deleteUserById(String id);
}

